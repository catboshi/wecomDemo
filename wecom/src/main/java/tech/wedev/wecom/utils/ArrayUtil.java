package tech.wedev.wecom.utils;

import java.util.ArrayList;
import java.util.List;

public final class ArrayUtil {
    private ArrayUtil() {

    }
    public static <T> T indexOf(T[] t, int index) {
        try {
            return t[index];
        } catch (IndexOutOfBoundsException ie) {
            return null;
        }
    }
    public static <T> Class<T> getArrayType(T[] t) { return (Class<T>) t.getClass().getComponentType();}
    public static <T> List<T> asArrayList (T... ts) {
        List<T> list = new ArrayList<>();
        if (ts != null) {
            for (T t : ts) {
                list.add (t);
            }
        }
        return list;
    }

    public static <T> List<T> asArrayNotNullList(T... ts) {
        List<T> list = new ArrayList<>();
        if (ts != null) {
            for (T t : ts) {
                if (t == null) {
                    continue;
                }
                list.add(t);
            }
        }
        return list;
    }
    public static <T> List<T> asArrayNotNull(T... ts) {
        List<T> list = new ArrayList<> ();
        if (ts != null) {
            for (T t : ts) {
                if (t == null) {
                    return new ArrayList<>();
                }
                list.add(t);
            }
        }
        return list;
    }

    public static <T> List<T> subList(List<T> list, Integer batchSize) {
        batchSize = BeanUtil.defaultIfNull(batchSize, 1000);
        if (list.size() > batchSize) {
            return list. subList(0, batchSize);
        } else {
            return list. subList(0, list.size ());
        }
    }
}
