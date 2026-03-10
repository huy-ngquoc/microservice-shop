package vn.edu.uit.msshop.product.shared.application.dto;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

// TODO: move this to common package of all services
public sealed interface Change<T>
        permits Change.Unchanged, Change.Set {

    static <T> Change<T> unchanged() {
        return new Unchanged<>();
    }

    static <T> Change<T> set(
            final T value) {
        return new Set<>(Objects.requireNonNull(value));
    }

    T apply(
            final T current);

    <R> R fold(
            Supplier<R> onUnchanged,
            Function<T, R> onSet);

    record Unchanged<T>() implements Change<T> {
        @Override
        public T apply(
                final T current) {
            return current;
        }

        @Override
        public <R> R fold(
                Supplier<R> onUnchanged,
                Function<T, R> onSet) {
            return onUnchanged.get();
        }
    }

    record Set<T>(
            T value) implements Change<T> {
        @Override
        public T apply(
                final T current) {
            return value;
        }

        @Override
        public <R> R fold(
                Supplier<R> onUnchanged,
                Function<T, R> onSet) {
            return onSet.apply(value);
        }
    }
}
