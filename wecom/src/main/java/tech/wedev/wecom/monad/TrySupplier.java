package tech.wedev.wecom.monad;

public interface TrySupplier<T>{
    T get() throws Throwable;
}
