/**
 * ychen. Copyright (c) 2016年11月19日.
 */
package cn.edu.fudan.iipl.flyvar.service.impl;

import java.io.Serializable;

import org.apache.commons.lang3.tuple.Pair;

/**
 * sample list vo class, be used to show sample list linked to flybase
 * 
 * @author racing
 * @version $Id: SampleLinkVO.java, v 0.1 2016年11月19日 下午1:39:05 racing Exp $
 */
public class SampleLinkVO implements Serializable {

    private static final long serialVersionUID = -3646204076883911181L;

    private String            linkNum;

    private String            sampleName;

    public SampleLinkVO() {
    }

    public SampleLinkVO(String linkNum, String sampleName) {
        this.linkNum = linkNum;
        this.sampleName = sampleName;
    }

    public String getLinkNum() {
        return linkNum;
    }

    public void setLinkNum(String linkNum) {
        this.linkNum = linkNum;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public static SampleLinkVO convertToSampleLinkVO(Pair<String, String> pair) {
        if (pair == null)
            return null;
        return new SampleLinkVO(pair.getLeft(), pair.getRight());
    }

}
