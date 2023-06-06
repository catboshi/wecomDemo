package tech.wedev.wecom.enums;

import java.util.Arrays;

public enum DefaultValEnum {
    INT_OBJECT(Integer.class, Integer.valueOf(0)),
    INT(int.class, 0),
    LONG_OBJECT(Long.class, Long.valueOf(0)),
    LONG(long.class, 0L),
    BOOLEAN_OBJECT(Boolean.class, Boolean.valueOf(false)),
    BOOLEAN(boolean.class, false);

    private Class<?> clazz;
    private Object defaultVal;

    DefaultValEnum(Class<?> clazz, Object defaultVal) {
        this.clazz = clazz;
        this.defaultVal = defaultVal;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public Object getDefaultVal() {
        return defaultVal;
    }

    public static DefaultValEnum getByCode(Class<?> clazz) {
        return Arrays.stream(DefaultValEnum.values()).filter(a -> clazz == a.clazz).findFirst().orElse(null);
    }
}
