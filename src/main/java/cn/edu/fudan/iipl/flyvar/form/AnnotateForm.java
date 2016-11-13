/**
 * ychen. Copyright (c) 2016年11月12日.
 */
package cn.edu.fudan.iipl.flyvar.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * annotate form
 * 
 * @author racing
 * @version $Id: AnnotateForm.java, v 0.1 2016年11月12日 下午11:33:18 racing Exp $
 */
public class AnnotateForm implements Serializable {

    private static final long serialVersionUID = 2562964373648381165L;

    /** 查询类型 */
    @NotNull(message = "{error.annotate.inputFormatType}")
    private Integer           inputFormatType;

    /** 查询输入 */
    private String            annotateInput;

    public Integer getInputFormatType() {
        return inputFormatType;
    }

    public void setInputFormatType(Integer inputFormatType) {
        this.inputFormatType = inputFormatType;
    }

    public String getAnnotateInput() {
        return annotateInput;
    }

    public void setAnnotateInput(String annotateInput) {
        this.annotateInput = annotateInput;
    }

}
