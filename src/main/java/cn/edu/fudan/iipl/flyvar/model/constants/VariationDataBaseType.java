/**
 * ychen. Copyright (c) 2016年10月29日.
 */
package cn.edu.fudan.iipl.flyvar.model.constants;

/**
 * 
 * @author racing
 * @version $Id: VariationDataBaseType.java, v 0.1 2016年10月29日 上午9:58:32 racing Exp $
 */
public enum VariationDataBaseType {

    DGRP(1, "DGRP", "freeze2_vcf_summary_205_flies"), 
    HUGO(2, "EMS screening", "all_hugo_snps_final_merged_vcf_summary_1homo4hetero"), 
    OTHER_PUBLIC(3, "Other public databases", "fly_public"), 
    DGRP_HUGO_OTHER(4, "DGRP + EMS screening + Other public databases", "dgrp_and_hugo");
    
    private int    code;
    
    private String desc;
    
    private String tableName;

    VariationDataBaseType(int code, String desc, String tableName) {
        this.code = code;
        this.desc = desc;
        this.tableName = tableName;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static VariationDataBaseType of(int code) {
        for (VariationDataBaseType item : values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

}
