package tech.wedev.wecom.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.function.Function;
public class ObjectUtils {
    private ObjectUtils() {
    }
    public static <T> T strToType(String src, Class<T> clazz) { return strToType(src, clazz, null); }

    public static <T> T strToType(String src, Class<T> clazz, Function<String, T> function) {

        if (StringUtils.isBlank(src)) {
            return null;
        }
        if (function != null) {
            return function.apply(src);
        }
        if (clazz == BigDecimal.class) {
            return (T) new BigDecimal(src);
        }
        try {
            if (clazz == Long.class) {
                return (T) Long.valueOf(src);
            }
            if (clazz == Integer.class) {
                return (T) Integer.valueOf(src);
            }
            if (clazz == Byte.class) {
                return (T) Byte.valueOf(src);
            }
        } catch (NumberFormatException n) {
            throw new NumberFormatException("数值格式化异常");
        }
        if (clazz == Boolean.class) {
            return (T) Boolean.valueOf(src);
        }
        throw new IllegalArgumentException("不支持转换");
    }

    public static <T> T strObjToType(Object src, Class<T> clazz) {
        return strObjToType(src, clazz, null);
    }
    public static <T> T strObjToType(Object src, Class < T > clazz, Function < String, T > function){
        if (src == null) {
            return null;
        }
        if (src instanceof String) {
            return strToType((String) src, clazz, function);
        }
        if (src.getClass() == clazz) {
            return (T) src;
        }
        throw new IllegalArgumentException("不是字符串对象");
    }
}

