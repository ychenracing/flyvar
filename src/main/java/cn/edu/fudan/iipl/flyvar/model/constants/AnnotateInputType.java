/**
 * ychen.
 * Copyright (c) 2016年11月13日.
 */
package cn.edu.fudan.iipl.flyvar.model.constants;


/**
 * input type for Annotate, such as "4 column tab separated" and "vcf format"
 * @author racing
 * @version $Id: AnnotateInputType.java, v 0.1 2016年11月13日 下午7:01:26 racing Exp $
 */
public enum AnnotateInputType {
    
    FOUR_COLUMN_TAB_SEPARATED(1, "4 column tab separated"),
    VCF_FORMAT(2, "vcf format");
    
    private int code;
    private String message;
    
    AnnotateInputType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static AnnotateInputType of(int code) {
        for (AnnotateInputType item : values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }

}
