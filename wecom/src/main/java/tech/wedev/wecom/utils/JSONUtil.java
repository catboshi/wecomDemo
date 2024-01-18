package tech.wedev.wecom.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JSONUtil {

    /**
     * json字符串转成java bean
     */
    public static <T> T convert2JavaBean(String jsonStr) {
        return (T) JSONObject.parseObject(jsonStr, Object.class);
    }

    /**
     * java bean字符串转成json
     */
    public static <T> String javaBeanConvert2JSONStr(T bean) {
        return JSONObject.toJSONString(bean);
    }

    /**
     * json字符串转成java bean list
     */
    public static <T> List<T> conver2BeanList(String jsonStr, Class<T> tClass) {
        return JSONObject.parseArray(jsonStr, tClass);
    }
}
