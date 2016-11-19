package cn.edu.fudan.iipl.flyvar.service.impl;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import cn.edu.fudan.iipl.flyvar.dao.SampleNameDao;
import cn.edu.fudan.iipl.flyvar.model.Variation;
import cn.edu.fudan.iipl.flyvar.model.constants.Constants;
import cn.edu.fudan.iipl.flyvar.service.CacheService;
import cn.edu.fudan.iipl.flyvar.service.SampleNameService;

@Service
public class SampleNameServiceImpl implements SampleNameService {

    private static final Logger  logger              = LoggerFactory
        .getLogger(SampleNameServiceImpl.class);

    private static final Pattern SAMPLE_NAME_PATTERN = Pattern.compile("\\D+(\\d+)\\D+");

    @Autowired
    private CacheService         cacheService;

    @Autowired
    private SampleNameDao        sampleNameDao;

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.SampleNameService#getSampleNames()
     */
    @Override
    public List<String> getSampleNames() {
        String key = Constants.CACHE_SAMPLE_NAME_LIST;
        List<String> result = cacheService.get(key);
        if (result == null) {
            result = sampleNameDao.getSampleNameList();
            cacheService.set(key, result);
        }
        return result;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.SampleNameService#getSampleNameSet()
     */
    @Override
    public Set<String> getSampleNameSet() {
        String key = Constants.CACHE_SAMPLE_NAME_SET;
        Set<String> result = cacheService.get(key);
        if (result == null) {
            result = Sets.newHashSet(sampleNameDao.getSampleNameList());
            cacheService.set(key, result);
        }
        return result;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.SampleNameService#getSampleNames(cn.edu.fudan.iipl.flyvar.model.Variation)
     */
    @Override
    public List<String> getSampleNames(Variation variation) {
        String key = new StringBuilder(Constants.CACHE_SAMPLE_NAME_LIST_FOR_VARIATION)
            .append(variation.getChr()).append("_").append(variation.getPos()).append("_")
            .append(variation.getRef()).append("_").append(variation.getAlt()).toString();
        // List<String> result = cacheService.get(key);
        List<String> result = null;
        if (result == null) {
            result = sampleNameDao.getSampleNamesContainTheVariation(variation);
            result.sort((sampleName1, sampleName2) -> {
                Matcher matcher = SAMPLE_NAME_PATTERN.matcher(sampleName1);
                Integer num1 = 0;
                Integer num2 = 0;
                if (matcher.find()) {
                    num1 = Integer.valueOf(matcher.group(1));
                }
                matcher.reset(sampleName2);
                if (matcher.find()) {
                    num2 = Integer.valueOf(matcher.group(1));
                }
                return num1.compareTo(num2);
            });
            cacheService.set(key, result);
        }
        return result;
    }

    /**
     * @see cn.edu.fudan.iipl.flyvar.service.SampleNameService#getSampleLinkPair(java.util.List)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Pair<String, String>> getSampleLinkPair(List<String> sampleNames) {
        return sampleNames.stream().map(sampleName -> {
            String key = new StringBuilder(Constants.CACHE_SAMPLE_LINK_PAIR_LIST).append(sampleName)
                .toString();
            // Object result = cacheService.get(key);
            Object result = null;
            if (result == null) {
                result = sampleNameDao.getSampleLinkNum(sampleName);
                if (result == null) {
                    String[] sampleNameFeature = sampleName.split("_");
                    if (ArrayUtils.isEmpty(sampleNameFeature) || sampleNameFeature.length < 2) {
                        return null;
                    }
                    String newSampleName = sampleNameFeature[0].toUpperCase() + "_"
                                           + sampleNameFeature[1];
                    result = ImmutablePair.of(null, newSampleName);
                    cacheService.set(key, result);
                    return (Pair<String, String>) result;
                } else {
                    cacheService.set(key, result);
                    return (Pair<String, String>) result;
                }
            } else if (result instanceof Pair) {
                return (Pair<String, String>) result;
            }
            return null;
        }).filter(pair -> pair != null).collect(Collectors.toList());
    }
}
