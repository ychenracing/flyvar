/**
 * ychen. Copyright (c) 2016年10月30日.
 */
package cn.edu.fudan.iipl.flyvar.service.impl;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.dao.QueryDao;
import cn.edu.fudan.iipl.flyvar.dao.SampleNameDao;
import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.VariationRegion;
import cn.edu.fudan.iipl.flyvar.model.constants.Constants;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.QueryService;

/**
 * 变异查询服务类
 * 
 * @author racing
 * @version $Id: QueryServiceImpl.java, v 0.1 2016年10月30日 下午9:12:55 racing Exp $
 */
@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryDao      queryDao;

    @Autowired
    private SampleNameDao sampleNameDao;

    @Autowired
    private CacheService  cacheService;

    private String getExistsCacheKey(Variation variation, String tableName) {
        return Constants.CACHE_VARIATION_EXIST_IN_DB + "_" + tableName + "_" + variation.getChr()
               + "_" + variation.getPos() + "_" + variation.getRef() + "_" + variation.getAlt();
    }

    private String getSampleNameListCacheKey(Variation variation, String tableName) {
        return Constants.CACHE_COUNT_SAMPLE_NAME_CONTAINS_VARIATION + "_" + tableName + "_"
               + variation.getChr() + "_" + variation.getPos() + "_" + variation.getRef() + "_"
               + variation.getAlt();
    }

    private String getRegionVariationsCacheKey(VariationRegion region, String tableName) {
        return Constants.CACHE_COUNT_SAMPLE_NAME_CONTAINS_VARIATION + "_" + tableName + "_"
               + region.getChr() + "_" + region.getStartPos() + "_" + region.getEndPos();
    }

    private Boolean existsFromCache(Variation variation, String tableName) {
        return cacheService.get(getExistsCacheKey(variation, tableName));
    }

    private List<String> getSampleNameListFromCache(Variation variation, String tableName) {
        return cacheService.get(getSampleNameListCacheKey(variation, tableName));
    }

    private List<Variation> getRegionVariationsFromCache(VariationRegion region, String tableName) {
        return cacheService.get(getRegionVariationsCacheKey(region, tableName));
    }

    private void putExistsToCache(Variation variation, String tableName, Boolean existsInCache) {
        String key = getExistsCacheKey(variation, tableName);
        cacheService.set(key, existsInCache);
    }

    private void putSampleNameListToCache(Variation variation, String tableName,
                                          List<String> sampleNameList) {
        String key = getSampleNameListCacheKey(variation, tableName);
        cacheService.set(key, sampleNameList);
    }

    private void putRegionVariationsToCache(VariationRegion region, List<Variation> variations,
                                            String tableName) {
        String key = getRegionVariationsCacheKey(region, tableName);
        cacheService.set(key, variations);
    }

    /*
     * @see cn.edu.fudan.iipl.flyvar.service.QueryService#queryByVariation(java.util.Collection,
     * cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType)
     */
    @Override
    public List<QueryResultVariation> queryByVariation(Collection<Variation> variations,
                                                       VariationDataBaseType variationDbType) {
        List<QueryResultVariation> result = variations.stream().filter(variation -> {
            Boolean existsInCache = existsFromCache(variation, variationDbType.getTableName());
            if (existsInCache == null) {
                existsInCache = queryDao.existsInDb(variation, variationDbType);
                putExistsToCache(variation, variationDbType.getTableName(), existsInCache);
            }
            return existsInCache.booleanValue();
        }).map(variation -> {
            QueryResultVariation resultVariation = new QueryResultVariation(variation.getChr(),
                variation.getPos(), variation.getRef(), variation.getAlt());
            List<String> sampleNameList = getSampleNameListFromCache(variation,
                variationDbType.getTableName());
            if (sampleNameList == null) {
                sampleNameList = sampleNameDao.getSampleNamesContainTheVariation(variation);
                putSampleNameListToCache(variation, variationDbType.getTableName(), sampleNameList);
            }
            if (EnumSet.of(VariationDataBaseType.DGRP, VariationDataBaseType.DGRP_HUGO_OTHER)
                .contains(variationDbType)) {
                resultVariation.setCount(sampleNameList.size());
            }
            return resultVariation;
        }).collect(Collectors.toList());
        return result == null ? Lists.newArrayList() : result;
    }

    /*
     * @see cn.edu.fudan.iipl.flyvar.service.QueryService#queryByRegion(java.util.Collection,
     * cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType)
     */
    @Override
    public List<QueryResultVariation> queryByRegion(Collection<VariationRegion> regions,
                                                    VariationDataBaseType variationDbType) {
        Set<Variation> varSet = regions.stream().map(region -> {

            List<Variation> variations = getRegionVariationsFromCache(region,
                variationDbType.getTableName());
            if (variations == null) {
                variations = queryDao.getVariationsByRegion(region, variationDbType);
                putRegionVariationsToCache(region, variations, variationDbType.getTableName());
            }
            return variations;
        }).flatMap(varList -> varList.stream()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(varSet)) {
            return Lists.newArrayList();
        }
        return queryByVariation(varSet, variationDbType);
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.QueryService#queryByGeneNameWholeRegion(java.util.Collection,
     *      cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType)
     */
    @Override
    public List<QueryResultVariation> queryByGeneNameWholeRegion(Collection<String> geneNames,
                                                                 VariationDataBaseType variationDbType) {

        return null;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.QueryService#queryByGeneNameExonRegion(java.util.Collection,
     *      cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType)
     */
    @Override
    public List<QueryResultVariation> queryByGeneNameExonRegion(Collection<String> geneNames,
                                                                VariationDataBaseType variationDbType) {
        return null;
    }

}
