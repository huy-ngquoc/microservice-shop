package vn.edu.uit.msshop.shared.application.dto;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public sealed interface Change<T> permits Change.Unchanged, Change.Set {

    static <T> Change<T> unchanged() {
        return new Unchanged<>();
    }

    static <T> Change<T> set(
            final T value) {
        return new Set<>(Objects.requireNonNull(value));
    }

    default @Nullable Unchanged<T> getUnchanged() {
        return null;
    }

    default @Nullable Set<T> getSet() {
        return null;
    }

    record Unchanged<T>() implements Change<T> {
        @Override
        public @Nullable Unchanged<T> getUnchanged() {
            return this;
        }
    }

    record Set<T>(
            T value) implements Change<T> {
        @Override
        public @Nullable Set<T> getSet() {
            return this;
        }
    }
}
