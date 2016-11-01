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

    private String getExistsCacheKey(Variation variation) {
        return Constants.CACHE_VARIATION_EXIST_IN_DB + variation.getChr() + "_" + variation.getPos()
               + "_" + variation.getRef() + "_" + variation.getAlt();
    }

    private String getSampleNameListCacheKey(Variation variation) {
        return Constants.CACHE_COUNT_SAMPLE_NAME_CONTAINS_VARIATION + variation.getChr() + "_"
               + variation.getPos() + "_" + variation.getRef() + "_" + variation.getAlt();
    }

    private Boolean existsFromCache(Variation variation) {
        return cacheService.get(getExistsCacheKey(variation));
    }

    private List<String> getSampleNameListFromCache(Variation variation) {
        return cacheService.get(getSampleNameListCacheKey(variation));
    }

    private void putExistsToCache(Variation variation, Boolean existsInCache) {
        String key = getExistsCacheKey(variation);
        cacheService.set(key, existsInCache);
    }

    private void putSampleNameListToCache(Variation variation, List<String> sampleNameList) {
        String key = getSampleNameListCacheKey(variation);
        cacheService.set(key, sampleNameList);
    }

    /* 
     * @see cn.edu.fudan.iipl.flyvar.service.QueryService#queryByVariation(java.util.Collection, 
     * cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType)
     */
    @Override
    public List<QueryResultVariation> queryByVariation(Collection<Variation> variations,
                                                       VariationDataBaseType variationDbType) {
        List<QueryResultVariation> result = variations.stream().filter(variation -> {
            Boolean existsInCache = existsFromCache(variation);
            if (existsInCache == null) {
                existsInCache = queryDao.existsInDb(variation, variationDbType);
                putExistsToCache(variation, existsInCache);
            }
            return existsInCache.booleanValue();
        }).map(variation -> {
            QueryResultVariation resultVariation = new QueryResultVariation(variation.getChr(),
                variation.getPos(), variation.getRef(), variation.getAlt());
            List<String> sampleNameList = getSampleNameListFromCache(variation);
            if (sampleNameList == null) {
                sampleNameList = sampleNameDao.getSampleNamesContainTheVariation(variation);
                putSampleNameListToCache(variation, sampleNameList);
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
        Set<Variation> varSet = regions.stream()
            .map(region -> queryDao.getVariationsByRegion(region, variationDbType))
            .flatMap(varList -> varList.stream()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(varSet)) {
            return Lists.newArrayList();
        }
        return queryByVariation(varSet, variationDbType);
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.QueryService#queryByGeneName(java.lang.String,
     *      cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType)
     */
    @Override
    public List<QueryResultVariation> queryByGeneName(String geneName,
                                                      VariationDataBaseType variationDbType) {
        return null;
    }

}
