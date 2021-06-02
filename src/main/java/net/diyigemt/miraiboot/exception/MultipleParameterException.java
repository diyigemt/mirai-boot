package net.diyigemt.miraiboot.exception;

/**
 * <h2>参数多余异常</h2>
 * @author Haythem
 * @since 1.0.0
 */
public class MultipleParameterException extends RuntimeException {
    public MultipleParameterException(){
        super();
    }

    public MultipleParameterException(String message){
        super(message);
    }
}
