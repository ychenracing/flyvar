/**
 * ychen. Copyright (c) 2016年11月19日.
 */
package cn.edu.fudan.iipl.flyvar.exception;

/**
 * invalid access exception
 * 
 * @author racing
 * @version $Id: InvalidAccessException.java, v 0.1 2016年11月19日 下午7:58:08 racing Exp $
 */
public class InvalidAccessException extends RuntimeException {

    private static final long serialVersionUID = 6487111942898804100L;

    public InvalidAccessException() {
    }

    public InvalidAccessException(String message) {
        super(message);
    }

    public InvalidAccessException(Throwable cause) {
        super(cause);
    }

    public InvalidAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccessException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
