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

    /** fotmat for variation input */
    @NotNull(message = "{error.annotate.inputFormatType}")
    private Integer           inputFormatType;

    /** variation input */
    private String            annotateInput;

    /**  */
    private String            annotateEmail;

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

    public String getAnnotateEmail() {
        return annotateEmail;
    }

    public void setAnnotateEmail(String annotateEmail) {
        this.annotateEmail = annotateEmail;
    }

}
