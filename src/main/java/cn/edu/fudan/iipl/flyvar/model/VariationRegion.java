/**
 * ychen. Copyright (c) 2016年11月1日.
 */
package cn.edu.fudan.iipl.flyvar.model;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.common.collect.Sets;

/**
 * 变异区域实体类
 * 
 * @author racing
 * @version $Id: VariationRegion.java, v 0.1 2016年11月1日 上午8:21:55 racing Exp $
 */
public class VariationRegion implements Serializable {

    private static final long serialVersionUID = 5002960678331634122L;

    private String            chr;
    private long              start;
    private long              end;

    public VariationRegion() {

    }

    public VariationRegion(String chr, long start, long end) {
        this.chr = chr;
        this.start = start;
        this.end = end;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public ImmutablePair<String, String> getCompatibleChrPair() {
        String prefix = "chr";
        String left = null;
        String right = null;
        if (this.chr.startsWith(prefix)) {
            left = this.chr;
            right = left.replaceFirst("chr", "");
        } else {
            right = this.chr;
            left = prefix + right;
        }
        ImmutablePair<String, String> result = new ImmutablePair<String, String>(left, right);
        return result;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * 转换输入的3 column格式的region字符串为VariationRegion实体类。
     * 
     * @param regionStr
     * @return null if regionStr is error format for VariationRegion.
     */
    public static Set<VariationRegion> convertInputToRegions(String regionStr) {
        String[] varSplitted = regionStr.split("\\s+");
        if (varSplitted.length % 3 != 0)
            return null;
        Set<VariationRegion> result = Sets.newLinkedHashSet();
        for (int i = 0; i < varSplitted.length; i++) {
            String chr = varSplitted[i++];
            String startPos = varSplitted[i++];
            String endPos = varSplitted[i];
            if (!startPos.matches("[0-9]+") || !endPos.matches("[0-9]+")) {
                return null;
            }
            VariationRegion variation = new VariationRegion(chr, Long.parseLong(startPos),
                Long.parseLong(endPos));
            result.add(variation);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
