package tech.wedev.wecom.monad;

public interface TryMapFunction<T, R> {
    R apply(T t) throws Throwable;
}