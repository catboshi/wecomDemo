package tech.wedev.wecom.exception;

public class ExceptionAssert {
    private ExceptionAssert() {

    }
    public static void isTrue (boolean express, ErrorCode errorCode) {
        if (express) {
            throw new WecomException (errorCode);
        }
    }
    public static void isFalse(boolean express, ErrorCode errorCode) {
        if (!express) {
            throw new WecomException (errorCode) ;
        }
    }
    public static <T> void isNull(T t, ErrorCode errorCode) {
        if (t == null) {
            throw new WecomException (errorCode);
        }
    }
    public static <T> void isNotNull(T t, ErrorCode errorCode) {
        if (t != null) {
            throw new WecomException (errorCode);
        }
    }
    public static void isTrue (boolean express, String message) {
        if (express) {
            throw new WecomException (ExceptionCode.INVALID_PARAMETER.getCode(), message);
        }
    }

    public static void isFalse (boolean express, String message) {
        if (!express) {
            throw new WecomException(ExceptionCode.INVALID_PARAMETER.getCode(), message) ;
        }
    }

    public static <T> void isNull(T t, String message) {
        if (t==null) {
            throw new WecomException(ExceptionCode.INVALID_PARAMETER.getCode(), message);
        }
    }

    public static<T> void isNotNull(T t, String message){
        if (t != null) {
            throw new WecomException(ExceptionCode.INVALID_PARAMETER.getCode(), message);
        }
    }

    public static <T> void isLegalLength(T t, int length, String message){
        if (t != null && t.toString().length() > length) {
            throw new WecomException(ExceptionCode.INVALID_PARAMETER.getCode(), message);
        }
    }

    public static void ifTrue (boolean express, String message) {
        if (express) {
            throw new BusinessException(message);
        }
    }

}
