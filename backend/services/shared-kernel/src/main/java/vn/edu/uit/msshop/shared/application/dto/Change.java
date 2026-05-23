package vn.edu.uit.msshop.shared.application.dto;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public sealed interface Change<T>
        permits
        Change.Unchanged,
        Change.Set {
    record ApplyChangeResult<T>(
            T newValue,
            boolean changed) {
        public static <T> ApplyChangeResult<T> unchanged(
                final T current) {
            return new ApplyChangeResult<>(current, false);
        }

        public static <T> ApplyChangeResult<T> set(
                final T newValue) {
            return new ApplyChangeResult<>(newValue, true);
        }
    }

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

    ApplyChangeResult<T> applyChange(
            final T current);

    record Unchanged<T>() implements Change<T> {
        @Override
        public @Nullable Unchanged<T> getUnchanged() {
            return this;
        }

        @Override
        public ApplyChangeResult<T> applyChange(
                final T current) {
            return ApplyChangeResult.unchanged(current);
        }
    }

    record Set<T>(
            T value) implements Change<T> {
        public static <T> ApplyChangeResult<T> applyChange(
                @Nullable
                final Set<T> set,

                final T current) {
            if (set == null) {
                return ApplyChangeResult.unchanged(current);
            }

            return set.applyChange(current);
        }

        @Override
        public @Nullable Set<T> getSet() {
            return this;
        }

        @Override
        public ApplyChangeResult<T> applyChange(
                final T current) {
            if (this.value.equals(current)) {
                return ApplyChangeResult.unchanged(current);
            }

            return ApplyChangeResult.set(this.value);
        }
    }
}
