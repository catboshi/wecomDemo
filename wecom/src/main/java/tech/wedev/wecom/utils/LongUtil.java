package tech.wedev.wecom.utils;

public class LongUtil {
    public static boolean isEmpty(Long value) {
        return !isNotEmpty(value);
    }

    public static boolean isNotEmpty(Long value) {
        return value != null && value.longValue() > 0L;
    }
}
