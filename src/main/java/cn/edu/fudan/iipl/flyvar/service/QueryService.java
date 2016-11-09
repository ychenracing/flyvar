/**
 * ychen. Copyright (c) 2016年10月30日.
 */
package cn.edu.fudan.iipl.flyvar.service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import cn.edu.fudan.iipl.flyvar.model.QueryResultVariation;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.VariationRegion;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;

/**
 * 
 * @author racing
 * @version $Id: QueryService.java, v 0.1 2016年10月30日 下午8:23:05 racing Exp $
 */
public interface QueryService {

    /**
     * Query by variation. Filter the variations who not exists in the variationDbType. If one
     * choose variationDatabase which contains "DGRP", then query by variation should count SAMPLE
     * NAME contains the variation.
     * 
     * @param variations
     * @param variationDbType
     * @return Filtered variations all of whom exist in the variationDbType. empty if variations are
     *         all not existing in the variationDbType.
     */
    public List<QueryResultVariation> queryByVariation(Collection<Variation> variations,
                                                       VariationDataBaseType variationDbType);

    /**
     * Query by region. Find all the variations who located in the region in variationDbType. If one
     * choose variationDatabase which contains "DGRP", then query by variation should count SAMPLE
     * NAME contains the variation.
     * 
     * @param regions
     * @param variationDbType
     * @return
     */
    public List<QueryResultVariation> queryByRegion(Collection<VariationRegion> regions,
                                                    VariationDataBaseType variationDbType);

    /**
     * Query by geneName Whole region. Find all the variations who located in the whole regions of
     * geneNames in variationDbType. If one choose variationDatabase which contains "DGRP", then
     * query by variation should count SAMPLE NAME contains the variation.
     * 
     * @param geneNames
     * @param variationDbType
     * @return
     */
    public List<QueryResultVariation> queryByGeneNameWholeRegion(Collection<String> geneNames,
                                                                 VariationDataBaseType variationDbType);

    /**
     * Query by geneName Exon region. Find all the variations who located in the exon regions of
     * geneNames in variationDbType. If one choose variationDatabase which contains "DGRP", then
     * query by variation should count SAMPLE NAME contains the variation.
     * 
     * @param geneNames
     * @param variationDbType
     * @return
     */
    public List<QueryResultVariation> queryByGeneNameExonRegion(Collection<String> geneNames,
                                                                VariationDataBaseType variationDbType);

    /**
     * annotate result variations. 
     * @param resultVariation
     * @return saved file path for vcf format of result variations before annotation.
     */
    public Path annotateResultVariation(Collection<QueryResultVariation> resultVariation);

}
