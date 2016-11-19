package cn.edu.fudan.iipl.flyvar.service;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import cn.edu.fudan.iipl.flyvar.model.Variation;

public interface SampleNameService {

    /**
     * 获取sample name列表
     */
    public List<String> getSampleNames();

    /**
     * 获取sample name列表 by variation
     */
    public List<String> getSampleNames(Variation variation);

    /**
     * 获取sample name的Set
     */
    public Set<String> getSampleNameSet();

    /**
     * get sample link pair which contains link num linked to flybase and sampleName.
     * 
     * @param sampleNames
     * @return
     */
    public List<Pair<String, String>> getSampleLinkPair(List<String> sampleNames);

}
