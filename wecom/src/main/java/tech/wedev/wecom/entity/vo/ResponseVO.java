package tech.wedev.wecom.entity.vo;

import tech.wedev.wecom.exception.ExceptionCode;

import java.io.Serializable;

public class ResponseVO<T> implements Serializable {
    private static final long serialVersionUID = -4518012376889558822L;

    private Integer retCode = ExceptionCode.SUCCESS.getCode();
    private String retMsg = ExceptionCode.SUCCESS.getMsg();

    private T data;

    public Integer getRetCode() {
        return retCode;
    }

    public void setRetCode(Integer retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseVO(Integer retCode, String retMsg) {
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public ResponseVO(Integer retCode, String retMsg, T data) {
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.data = data;
    }

    public ResponseVO(ExceptionCode ExceptionCode, T data) {
        this.retCode = ExceptionCode.getCode();
        this.retMsg = ExceptionCode.getMsg();
        this.data = data;
    }

    /**
     * 空内容失败，响应码500
     */
    public static <T> ResponseVO<T> error() {
        return new ResponseVO<T>(ExceptionCode.ERROR_500.getCode(),
                ExceptionCode.ERROR_500.getMsg(), null);
    }

    /**
     * 赋值内容失败，响应码500
     */
    public static <T> ResponseVO<T> error(T data) {
        return new ResponseVO<T>(ExceptionCode.ERROR_500.getCode(),
                ExceptionCode.ERROR_500.getMsg(), data);
    }

    /**
     * 赋值内容失败，响应信息自定义
     */
    public static <T> ResponseVO<T> error(String msg, T data) {
        return new ResponseVO<T>(ExceptionCode.ERROR_500.getCode(),
                msg, data);
    }

    /**
     * 赋值内容失败，响应信息自定义
     */
    public static <T> ResponseVO<T> error(Integer code, String msg, T data) {
        return new ResponseVO<T>(code, msg, data);
    }

    /**
     * 自定义内容失败
     */
    public static <T> ResponseVO<T> error(ExceptionCode ExceptionCode, T data) {
        return new ResponseVO<T>(ExceptionCode.getCode(), ExceptionCode.getMsg(), data);
    }

    /**
     * 赋值内容失败，响应码505
     */
    public static <T> ResponseVO<T> error(String msg) {
        return new ResponseVO<T>(ExceptionCode.FORBIDDEN_QYWX.getCode(),
                msg);
    }

    /**
     * 空内容成功，响应码200
     */
    public static <T> ResponseVO<T> success() {
        return new ResponseVO<T>(ExceptionCode.SUCCESS.getCode(),
                ExceptionCode.SUCCESS.getMsg(), null);
    }

    /**
     * 赋值内容成功，响应码200
     */
    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<T>(ExceptionCode.SUCCESS.getCode(),
                ExceptionCode.SUCCESS.getMsg(), data);
    }

    /**
     * 赋值内容成功，响应码200，自定义响应信息
     */
    public static <T> ResponseVO<T> success(String msg, T data) {
        return new ResponseVO<T>(ExceptionCode.SUCCESS.getCode(),
                msg, data);
    }

    /**
     * 赋值内容成功，响应码 0
     */
    public static <T> ResponseVO<T> success0(T data) {
        return new ResponseVO<T>(ExceptionCode.SUCCESS.getCode(),
                ExceptionCode.SUCCESS.getMsg(), data);
    }
}
