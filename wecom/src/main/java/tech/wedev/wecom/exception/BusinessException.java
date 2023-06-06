package tech.wedev.wecom.exception;

import org.apache.logging.log4j.message.ParameterizedMessageFactory;

public class BusinessException extends RuntimeException{
    public BusinessException() { super(); }
    public BusinessException (String message) { super (message); }
    public BusinessException (String message, Object... params) {
        super (ParameterizedMessageFactory.INSTANCE.newMessage (message, params).getFormattedMessage()) ;
    }
    public BusinessException(String message, Throwable cause) { super (message, cause); }
    public BusinessException (Throwable cause, String message, Object... params) {
        super (ParameterizedMessageFactory. INSTANCE .newMessage (message, params).getFormattedMessage(), cause);
    }
    public BusinessException (Throwable cause) {super(cause);}
    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super (message, cause, enableSuppression, writableStackTrace);
    }
}

