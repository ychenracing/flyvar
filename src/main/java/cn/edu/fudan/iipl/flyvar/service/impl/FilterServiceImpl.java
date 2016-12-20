package cn.edu.fudan.iipl.flyvar.service.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.edu.fudan.iipl.flyvar.common.AnnovarUtils;
import cn.edu.fudan.iipl.flyvar.common.FlyvarFileUtils;
import cn.edu.fudan.iipl.flyvar.common.PathUtils;
import cn.edu.fudan.iipl.flyvar.dao.QueryDao;
import cn.edu.fudan.iipl.flyvar.exception.CombineAnnotateResultException;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.constants.Constants;
import cn.edu.fudan.iipl.flyvar.model.constants.RemoveDispensableType;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;
import cn.edu.fudan.iipl.flyvar.service.AnnotateService;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.FilterService;
import cn.edu.fudan.iipl.flyvar.service.FlyvarMailSenderService;

@Service
public class FilterServiceImpl implements FilterService {

    @Autowired
    private CacheService            cacheService;

    @Autowired
    private QueryDao                queryDao;

    @Autowired
    private ThreadPoolTaskExecutor  taskExecutor;

    @Autowired
    private PathUtils               pathUtils;

    @Autowired
    private FlyvarMailSenderService mailSenderService;

    @Autowired
    private AnnotateService         annotateService;

    @Autowired
    private AnnovarUtils            annovarUtils;

    private String getExistsCacheKey(Variation variation, String tableName) {
        return Constants.CACHE_VARIATION_EXIST_IN_DB + tableName + "_" + variation.getChr() + "_"
               + variation.getPos() + "_" + variation.getRef() + "_" + variation.getAlt();
    }

    private Boolean existsFromCache(Variation variation, String tableName) {
        return cacheService.get(getExistsCacheKey(variation, tableName));
    }

    private void putExistsToCache(Variation variation, String tableName, Boolean existsInCache) {
        String key = getExistsCacheKey(variation, tableName);
        cacheService.set(key, existsInCache);
    }

    private String getExistsInDispensableGeneCacheKey(Variation variation) {
        return Constants.CACHE_VARIATION_EXIST_IN_DISPENSABLE_GENE + variation.getChr() + "_"
               + variation.getPos() + "_" + variation.getRef() + "_" + variation.getAlt();
    }

    private Boolean existsInDispensableGeneFromCache(Variation variation) {
        return cacheService.get(getExistsInDispensableGeneCacheKey(variation));
    }

    private void putExistsInDispensableGeneToCache(Variation variation, Boolean existsInCache) {
        String key = getExistsInDispensableGeneCacheKey(variation);
        cacheService.set(key, existsInCache);
    }

    @Override
    public List<Variation> filterVariations(Collection<Variation> variations,
                                            VariationDataBaseType dbType) {
        List<Variation> result = variations.stream().filter(variation -> {
            Boolean existsInCache = existsFromCache(variation, dbType.getTableName());
            if (existsInCache == null) {
                existsInCache = queryDao.existsInDb(variation, dbType);
                putExistsToCache(variation, dbType.getTableName(), existsInCache);
            }
            return existsInCache.booleanValue();
        }).collect(Collectors.toList());
        return result == null ? Lists.newArrayList() : result;
    }

    @Override
    public List<Variation> filterDispensableGeneVariations(Collection<Variation> variations) {
        List<Variation> result = variations.stream().filter(variation -> {
            Boolean existsInDispensableGene = existsInDispensableGeneFromCache(variation);
            if (existsInDispensableGene == null) {
                existsInDispensableGene = queryDao.existsInDispensableGene(variation);
                putExistsInDispensableGeneToCache(variation, existsInDispensableGene);
            }
            return existsInDispensableGene.booleanValue();
        }).collect(Collectors.toList());
        return result == null ? Lists.newArrayList() : result;
    }

    @Override
    public void asyncFilterAndSendEmail(Collection<Variation> variations,
                                        VariationDataBaseType dbType,
                                        RemoveDispensableType removeDispensable, String receiver) {
        taskExecutor.execute(() -> {
            List<Variation> filterResult = filterVariations(variations, dbType);
            if (removeDispensable == RemoveDispensableType.YES) {
                filterResult = filterDispensableGeneVariations(variations);
            }
            Path resultFilePath = FlyvarFileUtils
                .writeVariationsToFile(pathUtils.getAbsoluteAnnotationFilesPath(), filterResult);
            Map<String, Object> emailParams = Maps.newHashMap();
            emailParams.put("filterResult", resultFilePath.getFileName().toString());
            mailSenderService.sendFilterResults(emailParams, receiver);
        });
    }

    @Override
    public void asyncFilterAnnotateAndSendEmail(Collection<Variation> variations,
                                                VariationDataBaseType dbType,
                                                RemoveDispensableType removeDispensable,
                                                String receiver) {
        taskExecutor.execute(() -> {
            List<Variation> filterResult = filterVariations(variations, dbType);
            if (removeDispensable == RemoveDispensableType.YES) {
                filterResult = filterDispensableGeneVariations(variations);
            }
            Path vcfFilePath = annotateService.convertVariationsToVcfFile(filterResult);

            Path vcfPath = annotateService.annotateVcfFormatVariation(vcfFilePath);

            Path annovarInputPath = annovarUtils
                .getAnnovarInputPath(vcfPath.getFileName().toString());
            Path annotateResultPath = annovarUtils
                .getAnnotatePath(vcfPath.getFileName().toString());
            Path exonicAnnotatePath = annovarUtils
                .getExonicAnnotatePath(vcfPath.getFileName().toString());
            Path combineAnnovarOutPath = annovarUtils
                .getCombineAnnovarOutPath(vcfPath.getFileName().toString());
            Path annovarInvalidInputPath = annovarUtils
                .getAnnovarInvalidInputPath(vcfPath.getFileName().toString());

            Map<String, Object> emailParams = Maps.newHashMap();
            emailParams.put("annovarInput", annovarInputPath.getFileName().toString());
            emailParams.put("annotateResult", annotateResultPath.getFileName().toString());
            emailParams.put("exonicAnnotateResult", exonicAnnotatePath.getFileName().toString());
            if (exonicAnnotatePath.toFile().length() > 0) {
                Path combinedAnnotateResultPath = null;
                try {
                    combinedAnnotateResultPath = annotateService
                        .mergeAnnotateResult(vcfPath.getFileName().toString());
                    emailParams.put("combinedExonicResult",
                        combinedAnnotateResultPath.getFileName().toString());
                } catch (CombineAnnotateResultException ex) {
                }
            }
            emailParams.put("combineAnnovarOut", combineAnnovarOutPath.getFileName().toString());
            if (annovarInvalidInputPath.toFile().exists()) {
                emailParams.put("annovarInvalidInput",
                    annovarInvalidInputPath.getFileName().toString());
            }

            mailSenderService.sendAnnotateResults(emailParams, receiver);
        });
    }

}
