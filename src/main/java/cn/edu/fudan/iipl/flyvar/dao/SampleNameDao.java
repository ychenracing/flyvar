package cn.edu.fudan.iipl.flyvar.dao;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import cn.edu.fudan.iipl.flyvar.model.Variation;

@Repository
public class SampleNameDao {

    private static final Logger logger             = LoggerFactory.getLogger(SampleNameDao.class);

    private static final String SELECT_SAMPLE_NAME = "SELECT name FROM sample_name";

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

}
