package tech.wedev.wecom.utils;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArrayListUtil {

    /**
     * 判断为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 求交集
     * @param masterList 主数据列表
     * @param slaveList 从数据列表
     */
    public static <T> List<T> getIntersection(List<T> masterList, List<T> slaveList) {
        return masterList.stream().filter(slaveList::contains).collect(Collectors.toList());
    }

    /**
     * 求差集
     * @param masterList 主数据列表
     * @param slaveList 从数据列表
     */
    public static <T> List<T> getDifferenceSet(List<T> masterList, List<T> slaveList) {
        return masterList.stream().filter(t -> !slaveList.contains(t)).collect(Collectors.toList());
    }

    /**
     * 求并集
     * @param masterList 主数据列表
     * @param slaveList 从数据列表
     */
    public static <T> List<T> getUnionSet(List<T> masterList, List<T> slaveList) {
        return ImmutableList.<T>builder().addAll(masterList).addAll(slaveList).build().stream().distinct().collect(Collectors.toList());
    }

    /**
     * 通过列表中对象的某个字段进行去重
     * @param keyExtractor 对象属性
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
