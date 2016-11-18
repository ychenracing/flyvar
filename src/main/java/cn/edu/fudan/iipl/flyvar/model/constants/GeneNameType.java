/**
 * ychen.
 * Copyright (c) 2016年11月2日.
 */
package cn.edu.fudan.iipl.flyvar.model.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * gene name type for query by gene name in the query page.
 * 
 * @author racing
 * @version $Id: GeneNameType.java, v 0.1 2016年11月2日 上午8:42:19 racing Exp $
 */
public enum GeneNameType {

    FLYBASE_ID(1, "flyvase id", "FBgn\\d+"), 
    ANNOTATION_SYMBOL(2, "annotation symbol", "C[A-Z]\\d+"), 
    SYMBOL(3, "symbol", "[a-zA-Z].*");

    private int    code;
    private String desc;
    private String pattern;

    GeneNameType(int code, String desc, String pattern) {
        this.code = code;
        this.desc = desc;
        this.pattern = pattern;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public static GeneNameType of(int code) {
        if (code < 0)
            return null;
        for (GeneNameType item : values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }

    public static GeneNameType of(String geneName) {
        if (StringUtils.isBlank(geneName))
            return null;
        for (GeneNameType item : values()) {
            if (geneName.matches(item.getPattern())) {
                return item;
            }
        }
        return null;
    }

}
