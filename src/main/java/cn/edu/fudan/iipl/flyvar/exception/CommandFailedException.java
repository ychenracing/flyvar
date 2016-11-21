package cn.edu.fudan.iipl.flyvar.exception;

/**
 * command executed failed exception.
 * 
 * @author racing
 * @version $Id: CommandFailedException.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
public class CommandFailedException extends RuntimeException {

    private static final long serialVersionUID = 2786523003763337924L;

    public CommandFailedException() {
    }

    public CommandFailedException(String message) {
        super(message);
    }

    public CommandFailedException(Throwable cause) {
        super(cause);
    }

    public CommandFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandFailedException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
