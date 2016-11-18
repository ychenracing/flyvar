package cn.edu.fudan.iipl.flyvar.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.dao.QueryDao;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.constants.Constants;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.FilterService;

@Service
public class FilterServiceImpl implements FilterService {

    private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);

    @Autowired
    private CacheService        cacheService;

    @Autowired
    private QueryDao            queryDao;

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

}
