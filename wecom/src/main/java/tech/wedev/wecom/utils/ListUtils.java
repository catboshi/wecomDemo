package tech.wedev.wecom.utils;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ListUtils {
    private ListUtils() {

    }

    public static <T> T indexOf(List<T> t, int index) {
        return t.get(index);
    }

    public static <T> List<T> subList(List<T> t, Integer start, Integer end) {
        if (CollectionUtils.isEmpty(t)) {
            return t;
        }
        start = BeanUtils.defaultIfNull(start, 0);
        end = BeanUtils.defaultIfNull(end, t.size());
        if (start < 0) {
            start = 0;
        }
        if (start > t.size() - 1) {
            throw new RuntimeException("起始值大于实际长度");
        }
        if (start > end - 1) {
            throw new RuntimeException("起始值大于结束值");
        }
        if (end > t.size()) {
            end = t.size();
        }
        return t.subList(start, end);
    }

    /**
     * 通过列表中对象的某个字段进行去重
     * @param keyExtractor 对象属性
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        return t -> set.add(keyExtractor.apply(t));
    }
}
