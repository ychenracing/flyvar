/**
 * ychen. Copyright (c) 2016年11月13日.
 */
package cn.edu.fudan.iipl.flyvar.exception;

/**
 * 
 * @author racing
 * @version $Id: NotFoundException.java, v 0.1 2016年11月13日 下午2:23:57 racing Exp $
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -7241565137164790172L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable throwable) {
        super(throwable);
    }

}
