package net.cocoonmc.core.utils;

import java.util.Objects;

public class Pair<F, S> {

    private final F first;
    private final S second;

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> of(final F first, final S second) {
        return new Pair<>(first, second);
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Pair<?, ?>) {
            final Pair<?, ?> other = (Pair<?, ?>) obj;
            return Objects.equals(getFirst(), other.getFirst())
                    && Objects.equals(getSecond(), other.getSecond());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ',' + second + ')';
    }
}
