package tech.wedev.wecom.entity.vo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

public final class CommonResult<T> {
    private Boolean status;

    private T data;

    private String message;

    private String detailMessage;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public CommonResult() {
    }

    public CommonResult(Boolean status, T data, String message, String detailMessage) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    public CommonResult(Boolean status, T data, String message, String detailMessage,
                        String opType, Long uid, String param, Class<?> logClass, Object mapper) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.detailMessage = detailMessage;
        if (logClass != null && mapper != null) {
            try {
                Constructor<?> constructor = logClass.getConstructor();
                Object obj = constructor.newInstance();
                Field paramField = logClass.getDeclaredField("param");
                paramField.setAccessible(true);
                paramField.set(obj, param);

                Field resultField = logClass.getDeclaredField("result");
                resultField.setAccessible(true);
                resultField.set(obj, message);

                Field successField = logClass.getDeclaredField("success");
                successField.setAccessible(true);
                successField.set(obj, status ? 1 : 0);

                Field createUserField = logClass.getDeclaredField("userId");
                createUserField.setAccessible(true);
                createUserField.set(obj, uid);

                Field opTypeField = logClass.getDeclaredField("opType");
                opTypeField.setAccessible(true);
                opTypeField.set(obj, opType);

                Field opTimeField = logClass.getDeclaredField("opTime");
                opTimeField.setAccessible(true);
                opTimeField.set(obj, new Date());

                Method method = mapper.getClass().getMethod("insertSelective", logClass);
                method.invoke(mapper, obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(true, data, "请求成功", null);
    }

    public static <T> CommonResult<T> successLog(T data, String opType, Long uid, String param, Class<?> logClass, Object mapper) {
        return new CommonResult<>(true, data, "请求成功", null, opType, uid, param, logClass, mapper);
    }

    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(true, data, message, null);
    }


    public static <T> CommonResult<T> dataError(String message, T data) {
        return new CommonResult<>(false, data, message, null);
    }

    public static <T> CommonResult<T> dataError(String message) {
        return new CommonResult<>(false, null, message, null);
    }


    public static <T> CommonResult<T> dataMessage(String message) {
        return new CommonResult<>(true, null, message, null);
    }

    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(false, null, message, null);
    }

    public static CommonResult<Void> message(String message) {
        return new CommonResult<>(true, null, message, null);
    }

    public static <T> CommonResult<T> errorLog(String message,
                                               String opType, Long uid, String param, Class<?> logClass, Object mapper) {
        return new CommonResult<>(false, null, message, null, opType, uid, param, logClass, mapper);
    }

    public static CommonResult<Void> messageLog(String message,
                                                String opType, Long uid, String param, Class<?> logClass, Object mapper) {
        return new CommonResult<>(true, null, message, null, opType, uid, param, logClass, mapper);
    }

}
