package cn.edu.fudan.iipl.flyvar.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.model.Variation;

/**
 * SampleName database dao.
 * 
 * @author racing
 * @version $Id: SampleNameDao.java, v 0.1 2016年11月19日 下午6:37:16 racing Exp $
 */
@Repository
public class SampleNameDao {

    private static final Logger logger             = LoggerFactory.getLogger(SampleNameDao.class);

    private static final String SELECT_SAMPLE_NAME = "SELECT name FROM sample_name";

    private static final String SELECT_SAMPLE_LINK = "SELECT * FROM linkcontrast";

    @Autowired
    @Qualifier(value = "jdbcTemplate2")
    private JdbcTemplate        jdbcTemplate2;

    /**
     * 获取sample_name列表
     * 
     * @return
     */
    public List<String> getSampleNameList() {
        List<String> result = jdbcTemplate2.queryForList(SELECT_SAMPLE_NAME, String.class);
        return result == null ? Lists.newArrayList() : result;
    }

    /**
     * get all sample name that the sample name table contains the variation.
     * 
     * @param variation
     * @return
     */
    public List<String> getSampleNamesContainTheVariation(Variation variation) {
        List<String> sampleTableNames = getSampleNameList();
        if (!CollectionUtils.isEmpty(sampleTableNames)) {
            return sampleTableNames.stream().filter(sampleTableName -> {
                StringBuilder sql = new StringBuilder("select chr from ").append(sampleTableName)
                    .append(" where (chr=? or chr=?) and pos=? and ref=? and alt=? limit 1");
                ImmutablePair<String, String> compatibleChr = variation.getCompatibleChrPair();
                List<?> result = jdbcTemplate2.queryForList(sql.toString(), compatibleChr.left,
                    compatibleChr.right, variation.getPos(), variation.getRef(),
                    variation.getAlt());
                return !CollectionUtils.isEmpty(result);
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * get sample link and sample name linked to flybase by sampleName.
     * 
     * @param sampleName
     * @return
     */
    public Pair<String, String> getSampleLinkNum(String sampleName) {
        if (StringUtils.isBlank(sampleName) || !sampleName.contains("_"))
            return null;
        String[] sampleNameFeature = sampleName.split("_");
        if (ArrayUtils.isEmpty(sampleNameFeature) || sampleNameFeature.length < 2) {
            return null;
        }
        sampleName = sampleNameFeature[0].toUpperCase() + "_" + sampleNameFeature[1];

        String sql = SELECT_SAMPLE_LINK + " where samplename ='" + sampleName + "' limit 1";
        List<Pair<String, String>> result = jdbcTemplate2.query(sql, (rs, rowNum) -> {
            String linkNum = rs.getString("link");
            String simplifiedSampleName = rs.getString("samplename");
            return ImmutablePair.of(linkNum, simplifiedSampleName);
        });
        if (CollectionUtils.isEmpty(result))
            return null;
        return result.get(0);
    }

    /**
     * get sample link and sample name linked to flybase by sampleNames.
     * 
     * @param sampleNames
     * @return
     */
    public List<Pair<String, String>> getSampleLinkNum(List<String> sampleNames) {
        String jointSampleNames = StringUtils.join(sampleNames.stream().map(sampleName -> {
            String[] sampleNameFeature = sampleName.split("_");
            if (ArrayUtils.isEmpty(sampleNameFeature) || sampleNameFeature.length < 2) {
                return null;
            }
            return "'" + sampleNameFeature[0].toUpperCase() + "_" + sampleNameFeature[1] + "'";
        }).collect(Collectors.toList()), ",");
        String sql = SELECT_SAMPLE_LINK + " where samplename in (" + jointSampleNames + ")";
        List<Pair<String, String>> result = jdbcTemplate2.query(sql, (rs, rowNum) -> {
            String linkNum = rs.getString("link");
            String sampleName = rs.getString("samplename");
            return ImmutablePair.of(linkNum, sampleName);
        });
        return result == null ? Lists.newArrayList() : result;
    }

}
