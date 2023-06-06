package tech.wedev.wecom.exception;

public class WecomException extends RuntimeException{
    private int code;
    private String msg;

    public WecomException (ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.msg = errorCode.getMsg();
        this.code = errorCode.getCode ();
    }

    public WecomException(int code, String message) {
            super (message);
            this.msg = message;
            this.code = code;
    }
    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
