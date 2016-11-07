/**
 * ychen. Copyright (c) 2016年10月27日.
 */
package cn.edu.fudan.iipl.flyvar.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 查询表单
 * 
 * @author racing
 * @version $Id: QueryForm.java, v 0.1 2016年10月27日 下午10:52:16 racing Exp $
 */
public class FilterForm implements Serializable {

    private static final long serialVersionUID = -3614043285954348983L;

    /** 变异数据库 */
    @NotNull(message = "{error.filter.variationDb}")
    private Integer           variationDb;

    /** 查询类型 */
    @NotNull(message = "{error.filter.inputFormatType}")
    private Integer           inputFormatType;

    /** 是否移除dispensible gene的variation */
    @NotNull(message = "{error.filter.removeDispensable}")
    private Integer           removeDispensable;

    /** 查询输入 */
    private String            filterInput;

    private String            filterEmail;

    public Integer getVariationDb() {
        return variationDb;
    }

    public void setVariationDb(Integer variationDb) {
        this.variationDb = variationDb;
    }

    public Integer getInputFormatType() {
        return inputFormatType;
    }

    public void setInputFormatType(Integer inputFormatType) {
        this.inputFormatType = inputFormatType;
    }

    public Integer getRemoveDispensable() {
        return removeDispensable;
    }

    public void setRemoveDispensable(Integer removeDispensable) {
        this.removeDispensable = removeDispensable;
    }

    public String getFilterInput() {
        return filterInput;
    }

    public void setFilterInput(String filterInput) {
        this.filterInput = filterInput;
    }

    public String getFilterEmail() {
        return filterEmail;
    }

    public void setFilterEmail(String filterEmail) {
        this.filterEmail = filterEmail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
