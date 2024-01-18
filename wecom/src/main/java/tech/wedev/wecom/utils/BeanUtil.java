package tech.wedev.wecom.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class BeanUtil {
    private BeanUtil() {

    }
    public static <T> T defaultIfNull(T src, T def) {
        if (src == null) {
            return def;
        }
        return src;
    }
    public static <T> T defaultGetOne (List<T> src) {
        if (src != null && src.size() == 1) {
            return src.get (0);
        }
        if (src.size() > 1) {
            throw new RuntimeException(String.format ("src大于1个 , size:%s", src.size()));
        }
        return null;

    }
    public static <T> T defaultGetByOne (List<T> sre) {
        if (sre != null && sre.size() >= 1) {
            return sre.get (0);
        }
        return null;
    }
    public static <S, T> T copyProperty (S src, Class<T> tarClazz) {
        return copyProperty(src, tarClazz, (String[]) null);
    }

    public static <T,S> T copyProperty(S src, Class<T> tarClazz, String ... ignoreProperties) {
        try {
            final T t = tarClazz.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(src, t, ignoreProperties);
            return t;
        } catch (Exception e) {
                throw new RuntimeException(e);
        }
    }
    public static <T,S> T copyProperty(S src, Class<T> tarClazz, Class<?> editable){
        try{
            final T t=tarClazz.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(src,t,editable);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static <T,S> List<T> copyProperties(List<S> srcs, Class<T> tarClazz) {
        return copyProperties(srcs, tarClazz, (String[])null);
    }

    public static <T, S> List<T> copyProperties(List<S> srcs, Class<T> tarClazz, String ... ignoreProperties) {
        if (srcs == null) {
            return new ArrayList<>();
        }
        return srcs.stream().map(a -> copyProperty(a, tarClazz, ignoreProperties)).collect (Collectors.toList());
    }

    public static <T, S> List<T> copyProperties (List<S> srcs, Class<T> tarClazz, Class<?> editable) {
        if (srcs == null) {
            return new ArrayList<>();
        }
        return srcs.stream().map(a -> copyProperty(a, tarClazz, editable)).collect (Collectors.toList());
    }
}
