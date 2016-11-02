/**
 * ychen. Copyright (c) 2016年10月29日.
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
 * 变异实体类（SNP、INDEL）
 * 
 * @author racing
 * @version $Id: Variation.java, v 0.1 2016年10月29日 下午11:13:19 racing Exp $
 */
public class Variation implements Comparable<Variation>, Serializable {

    private static final long serialVersionUID = -2994031807739119861L;

    private String            chr;
    private long              pos;
    private String            ref;
    private String            alt;
    private int               count;
    private boolean           existsInVariationDb;
    private String            annotation;

    public Variation() {
    }

    public Variation(String chr, long pos, String ref, String alt) {
        this.chr = chr;
        this.pos = pos;
        this.ref = ref;
        this.alt = alt;
    }

    public String getChr() {
        return chr;
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

    public void setChr(String chr) {
        this.chr = chr;
    }

    public long getPos() {
        return pos;
    }

    public void setPos(long pos) {
        this.pos = pos;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isExistsInVariationDb() {
        return existsInVariationDb;
    }

    public void setExistsInVariationDb(boolean existsInVariationDb) {
        this.existsInVariationDb = existsInVariationDb;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    /**
     * 转换输入的4 column格式的variation字符串为Variation实体类。
     * 
     * @param variationStr
     * @return null if variationStr is error format for Variation.
     */
    public static Set<Variation> convertInputToVariations(String variationStr) {
        String[] varSplitted = variationStr.split("\\s+");
        if (varSplitted.length % 4 != 0)
            return null;
        Set<Variation> result = Sets.newLinkedHashSet();
        for (int i = 0; i < varSplitted.length; i++) {
            String chr = varSplitted[i++];
            String pos = varSplitted[i++];
            String ref = varSplitted[i++];
            String alt = varSplitted[i];
            if (!pos.matches("[0-9]+") || !ref.matches("[ATCG]+") || !alt.matches("[ATCG]+")) {
                return null;
            }
            Variation variation = new Variation(chr, Long.parseLong(pos), ref, alt);
            result.add(variation);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this,
            new String[] { "count", "judge", "annotation" });
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other,
            new String[] { "count", "judge", "annotation" });
    }

    @Override
    public int compareTo(Variation other) {
        if (this.getPos() < other.getPos()) {
            return -1;
        } else if (this.getPos() == other.getPos()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
