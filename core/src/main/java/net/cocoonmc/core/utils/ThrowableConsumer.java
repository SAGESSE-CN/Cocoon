package net.cocoonmc.core.utils;

import java.util.Objects;

public interface ThrowableConsumer<T> {

    void accept(T t) throws Exception;

    default ThrowableConsumer<T> andThen(ThrowableConsumer<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
