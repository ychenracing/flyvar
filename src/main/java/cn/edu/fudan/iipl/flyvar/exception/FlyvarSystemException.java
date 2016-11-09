package cn.edu.fudan.iipl.flyvar.exception;

/**
 * FlyVar系统级异常
 * @author yorkchen
 * @since 2016-11-08
 */
public class FlyvarSystemException extends RuntimeException {

    private static final long serialVersionUID = -8077483603832415639L;

    public FlyvarSystemException() {
        super();
    }

    public FlyvarSystemException(String message) {
        super(message);
    }

    public FlyvarSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlyvarSystemException(Throwable cause) {
        super(cause);
    }
}
