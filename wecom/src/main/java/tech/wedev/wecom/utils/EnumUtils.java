package tech.wedev.wecom.utils;

import tech.wedev.wecom.enums.BaseIntegerEnum;
import tech.wedev.wecom.enums.BaseStringEnum;

import java.util.Arrays;
import java.util.Optional;

public final class EnumUtils {
    private EnumUtils() {

    }

    public static <T extends BaseIntegerEnum> T getByIntCode(Class<T> tClass, Integer code) {
        return Arrays.stream(tClass.getEnumConstants()).filter(a -> a.getCode().equals(code)).findFirst().orElse(null);
    }

    public static <T extends BaseStringEnum> T getByStringCode(Class<T> tClass, String code) {
        return Arrays.stream(tClass.getEnumConstants()).filter(a -> a.getCode().equals(code)).findFirst().orElse(null);
    }

    public static <T extends BaseIntegerEnum> String getDescByIntCode(Class<T> tClass, Integer code) {
        return Arrays.stream(tClass.getEnumConstants()).filter(a -> a.getCode().equals(code)).map(a->a.getDesc()).findFirst().orElse(null);
    }

    public static <T extends BaseStringEnum> String getDescByStringCode(Class<T> tClass, String code) {
        return Arrays.stream(tClass.getEnumConstants()).filter(a -> a.getCode().equals(code)).map(a->a.getDesc()).findFirst().orElse(null);
    }

    public static <T extends BaseIntegerEnum> String getDescByInt(T enumObj) {
        return Optional.ofNullable(enumObj).map(a -> a.getDesc()).orElse(null);
    }

    public static <T extends BaseStringEnum> String getDescByString(T enumObj) {
        return Optional.ofNullable(enumObj).map(a -> a.getDesc()).orElse(null);
    }
}
