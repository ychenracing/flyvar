/**
 * ychen. Copyright (c) 2016年10月30日.
 */
package cn.edu.fudan.iipl.flyvar.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.VariationRegion;
import cn.edu.fudan.iipl.flyvar.model.constants.VariationDataBaseType;

/**
 * 
 * @author racing
 * @version $Id: QueryDao.java, v 0.1 2016年10月30日 下午9:16:36 racing Exp $
 */
@Repository
public class QueryDao {

    private static final String FLYBASE_ID_TABLE          = "cg_id_convert";

    private static final String GENENAME_TABLE            = "dmel_all_r5_12_refgene";

    private static final String GENENAME_FLYBASE_ID_TABLE = "fbgn_fbtr";

    private static final String DISPENSABLE_GENE_TABLE    = "startend";

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate        flyvarJdbcTemplate;

    /**
     * Judge if the variation exists in db or not.
     * 
     * @param variation
     * @param dbType
     * @return
     */
    public boolean existsInDb(Variation variation, VariationDataBaseType dbType) {
        String sql = "select chr from " + dbType.getTableName()
                     + " where (chr=? or chr=?) and pos=? and ref=? and alt=? and homo != 0 limit 1";
        ImmutablePair<String, String> compatibleChr = variation.getCompatibleChrPair();
        List<?> result = flyvarJdbcTemplate.queryForList(sql, compatibleChr.left,
            compatibleChr.right, variation.getPos(), variation.getRef(), variation.getAlt());
        return !CollectionUtils.isEmpty(result);
    }

    public boolean existsInDispensableGene(Variation variation) {
        String sql = "select chr from " + DISPENSABLE_GENE_TABLE
                     + " where (chr=? or chr=?) and ? between start and end limit 1";
        ImmutablePair<String, String> compatibleChr = variation.getCompatibleChrPair();
        List<?> result = flyvarJdbcTemplate.queryForList(sql, compatibleChr.left,
            compatibleChr.right, variation.getPos());
        return !CollectionUtils.isEmpty(result);
    }

    public List<Variation> getVariationsByRegion(VariationRegion region,
                                                 VariationDataBaseType dbType) {
        String sql = "select chr, pos, ref, alt from " + dbType.getTableName()
                     + " where (chr=? or chr=?) and pos between ? and ? and homo != 0";
        ImmutablePair<String, String> compatibleChr = region.getCompatibleChrPair();
        List<Variation> result = flyvarJdbcTemplate.query(sql, new RowMapper<Variation>() {
            @Override
            public Variation mapRow(ResultSet rs, int rowNum) throws SQLException {
                String chr = rs.getString("chr");
                long pos = rs.getLong("pos");
                String ref = rs.getString("ref");
                String alt = rs.getString("alt");
                Variation variation = new Variation(chr, pos, ref, alt);
                variation.setExistsInVariationDb(true);
                return variation;
            }
        }, compatibleChr.left, compatibleChr.right, region.getStart(), region.getEnd());
        return result == null ? Lists.newArrayList() : result;
    }

    /**
     * get flybase_id by annotation symbol in query by gene name.
     * <ul><li>CG1631 -> FBgn0031101</li>
     * <li>CG1555 -> FBgn0000337</li><ul>
     * @param annotationSymbols
     * @return
     */
    public List<String> getFlybaseIdByAnnotationSymbol(Collection<String> annotationSymbols) {
        if (CollectionUtils.isEmpty(annotationSymbols))
            return Lists.newArrayList();
        String sql = "select flybase_id from " + FLYBASE_ID_TABLE + " where submitted_id in (\""
                     + StringUtils.join(annotationSymbols, "\", \"") + "\")";
        List<String> result = flyvarJdbcTemplate.queryForList(sql, String.class);
        return result == null ? Lists.newArrayList() : result;
    }

    /**
     * get flybase_id by symbol in query by gene name.
     * <ul><li>CG1631 -> FBgn0031101</li>
     * <li>cn -> FBgn0000337</li><ul>
     * @param symbols
     * @return
     */
    public List<String> getFlybaseIdBySymbol(Collection<String> symbols) {
        if (CollectionUtils.isEmpty(symbols))
            return Lists.newArrayList();
        String sql = "select flybase_id from " + FLYBASE_ID_TABLE + " where symbol in (\""
                     + StringUtils.join(symbols, "\", \"") + "\")";
        List<String> result = flyvarJdbcTemplate.queryForList(sql, String.class);
        return result == null ? Lists.newArrayList() : result;
    }

    public List<VariationRegion> getVariationRegionWholeRegion(Collection<String> geneNames) {
        if (CollectionUtils.isEmpty(geneNames))
            return Lists.newArrayList();
        String sql = "select chr, start, end from " + GENENAME_TABLE + " where genename in (\""
                     + StringUtils.join(geneNames, "\", \"") + "\")";
        List<VariationRegion> result = flyvarJdbcTemplate.query(sql,
            new BeanPropertyRowMapper<VariationRegion>(VariationRegion.class));
        return result == null ? Lists.newArrayList() : result;
    }

    public List<VariationRegion> getVariationRegionExonRegion(Collection<String> geneNames) {
        if (CollectionUtils.isEmpty(geneNames))
            return Lists.newArrayList();
        String sql = "select chr, sumexonstart, sumexonend from " + GENENAME_TABLE
                     + " where genename in (\"" + StringUtils.join(geneNames, "\", \"") + "\")";
        List<VariationRegion> result = flyvarJdbcTemplate
            .query(sql, new RowMapper<List<VariationRegion>>() {
                @Override
                public List<VariationRegion> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String chr = rs.getString("chr");
                    String[] starts = rs.getString("sumexonstart").split(",");
                    String[] ends = rs.getString("sumexonend").split(",");
                    List<VariationRegion> result = Lists.newArrayList();
                    if (starts.length != ends.length)
                        return result;
                    for (int i = 0; i < starts.length; i++) {
                        VariationRegion item = new VariationRegion(chr, Long.parseLong(starts[i]),
                            Long.parseLong(ends[i]));
                        result.add(item);
                    }
                    return result;
                }
            }).stream().flatMap(regions -> regions.stream()).collect(Collectors.toList());
        return result == null ? Lists.newArrayList() : result;
    }

    public List<String> getGeneNamesByFlybaseId(Collection<String> flybaseIds) {
        if (CollectionUtils.isEmpty(flybaseIds))
            return Lists.newArrayList();
        String sql = "select distinct fbtr from " + GENENAME_FLYBASE_ID_TABLE + " where fbgn in (\""
                     + StringUtils.join(flybaseIds, "\", \"") + "\")";
        List<String> result = flyvarJdbcTemplate.queryForList(sql, String.class);
        return result == null ? Lists.newArrayList() : result;
    }

}
