package vn.edu.uit.msshop.product.shared.application.dto;

import java.util.Objects;

// TODO: move this to common package of all services
public sealed interface Change<T>
        permits Change.Unchanged, Change.Set {

    T apply(
            final T current);

    static <T> Change<T> unchanged() {
        return new Unchanged<>();
    }

    static <T> Change<T> set(
            final T value) {
        return new Set<>(Objects.requireNonNull(value));
    }

    record Unchanged<T>() implements Change<T> {
        @Override
        public T apply(
                final T current) {
            return current;
        }
    }

    record Set<T>(
            T value) implements Change<T> {
        @Override
        public T apply(
                final T current) {
            return value;
        }
    }
}
