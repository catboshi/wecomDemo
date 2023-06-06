package tech.wedev.wecom.exception;

public enum ExceptionCode implements ErrorCode{
    SUCCESS(200,"成功"),
    INVALID_PARAMETER(403, "无效的参数"),
    REQUEST_ERROR(500,"请求企业微信API异常"),
    ERROR_500(500,"系统异常"),
    ;


    private final Integer code;
    private final String msg;
    ExceptionCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
