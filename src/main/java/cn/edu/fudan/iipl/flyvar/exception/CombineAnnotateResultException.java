package cn.edu.fudan.iipl.flyvar.exception;

/**
 * annotate result combined exception, this aim to combine variant_function and exonic_variant_function.
 * 
 * @author racing
 * @version $Id: CombineAnnotateResultException.java, v 0.1 2016年11月09日 下午13:37:16 racing Exp $
 */
public class CombineAnnotateResultException extends RuntimeException {

    private static final long serialVersionUID = 4757931457277785817L;

    public CombineAnnotateResultException() {
        super();
    }

    public CombineAnnotateResultException(String message) {
        super(message);
    }

    public CombineAnnotateResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public CombineAnnotateResultException(Throwable cause) {
        super(cause);
    }
}
