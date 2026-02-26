package vn.edu.uit.msshop.account.application.common;
import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;



@NullUnmarked
public sealed interface Patch<T> permits Patch.Unchanged, Patch.Set, Patch.Clear {
    T apply(
            final T current);

    static <T> @NonNull Patch<T> unchanged() {
        return new Unchanged<>();
    }

    static <T> @NonNull Patch<T> clear() {
        return new Clear<>();
    }

    @NullMarked
    static <T> Patch<T> set(
            final T value) {
        return new Set<>(Objects.requireNonNull(value));
    }

    record Unchanged<T>() implements Patch<T> {
        public T apply(
                final T current) {
            return current;
        }
    }

    record Clear<T>() implements Patch<T> {
        public T apply(
                final T current) {
            return null;
        }
    }

    record Set<T>(
            T value) implements Patch<T> {
        public T apply(
                final T current) {
            return value;
        }
    }
}
