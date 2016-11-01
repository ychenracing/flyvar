/**
 * ychen. Copyright (c) 2016年10月27日.
 */
package cn.edu.fudan.iipl.flyvar.form;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 查询表单
 * 
 * @author racing
 * @version $Id: QueryForm.java, v 0.1 2016年10月27日 下午10:52:16 racing Exp $
 */
public class QueryForm {

    /** 变异数据库 */
    @NotNull(message = "{error.variationDb}")
    private Integer variationDb;

    /** 查询类型 */
    @NotNull(message = "{error.queryType}")
    private Integer queryType;

    /** 查询输入 */
    private String  queryInput;

    /** 选择的sample name */
    private String  selectSample;

    /** 接受结果的邮箱 */
    private String  queryEmail;

    public Integer getVariationDb() {
        return variationDb;
    }

    public void setVariationDb(Integer variationDb) {
        this.variationDb = variationDb;
    }

    public Integer getQueryType() {
        return queryType;
    }

    public void setQueryType(Integer queryType) {
        this.queryType = queryType;
    }

    public String getQueryInput() {
        return queryInput;
    }

    public void setQueryInput(String queryInput) {
        this.queryInput = queryInput;
    }

    public String getSelectSample() {
        return selectSample;
    }

    public void setSelectSample(String selectSample) {
        this.selectSample = selectSample;
    }

    public String getQueryEmail() {
        return queryEmail;
    }

    public void setQueryEmail(String queryEmail) {
        this.queryEmail = queryEmail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
