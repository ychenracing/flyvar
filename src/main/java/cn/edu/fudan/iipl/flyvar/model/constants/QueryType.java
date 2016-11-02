/**
 * ychen. Copyright (c) 2016年10月29日.
 */
package cn.edu.fudan.iipl.flyvar.model.constants;

/**
 * 
 * @author racing
 * @version $Id: QueryType.java, v 0.1 2016年10月29日 上午10:02:49 racing Exp $
 */
public enum QueryType {

    VARIATION(1, "by variation"), 
    SAMPLE(2, "by sample"), 
    REGION(3, "by region"), 
    GENE_WHOLE(4, "by gene name(whole region)"), 
    GENE_EXON(5, "by gene name (exon region)");
    
    private int    code;
    
    private String desc;

    QueryType(int code, String desc) {
        this.code = code;
        this.desc = desc;
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

    public static QueryType of(int code) {
        for (QueryType item : QueryType.values()) {
            if (item.getCode() == code) {
                return item;
            }
        }
        return null;
    }

}
