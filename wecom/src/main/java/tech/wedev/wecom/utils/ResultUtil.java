package tech.wedev.wecom.utils;

import tech.wedev.wecom.entity.common.Result;

public class ResultUtil {

    /**
     * 替代success，JSONUtils.javaBeanConvert2JSONStr 对日期解析不是期望的格式
     */
    public static Result SUCCESS(Object object) {
        Result result = new Result();
        result.setCode("0");
        result.setMsg("调用成功");
        result.setData(object);
        return result;
    }

    public static Result SUCCESS() {
        return SUCCESS(null);
    }

    /**
     * 替代error，JSONUtils.javaBeanConvert2JSONStr 对日期解析不是期望的格式
     */
    public static Result ERROR(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result ERROR(String msg) {
        return ERROR("-1", msg);
    }

    public static String success(Object object) {
        Result result = new Result();
        result.setCode("0");
        result.setMsg("调用成功");
        result.setData(object);
        return JSONUtil.javaBeanConvert2JSONStr(result);
    }

    public static String success(Object object, int count) {
        Result result = new Result();
        result.setCode("0");
        result.setMsg("调用成功");
        result.setPageCount(count);
        result.setData(object);
        return JSONUtil.javaBeanConvert2JSONStr(result);
    }

    public static String success() {
        return success(null);
    }

    //当发生异常时，根据code可判断异常的类型
    //这里发生异常的error方法，未定义返回数据，默认为null
    public static String error(String msg) {
        Result result = new Result();
        result.setCode("-1");
        result.setMsg(msg);
        return JSONUtil.javaBeanConvert2JSONStr(result);
    }

    //当发生异常时，根据code可判断异常的类型
    //这里发生异常的error方法，未定义返回数据，默认为null
    public static String error(String code, String msg) {
        Result result = new Result();
        result.setCode("-1");
        result.setMsg(msg);
        return JSONUtil.javaBeanConvert2JSONStr(result);
    }
}
