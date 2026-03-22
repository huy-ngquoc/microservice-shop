package vn.edu.uit.msshop.product.variant.domain.model;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.shared.domain.Domains;

public record VariantDeletionTime(
        Instant value) {
    public VariantDeletionTime {
        Domains.requireNonNull(value, "Deletion time value CANNOT be null");
    }

    public static VariantDeletionTime now() {
        return new VariantDeletionTime(Instant.now());
    }

    public static @Nullable VariantDeletionTime ofNullable(
            @Nullable
            final Instant value) {
        if (value == null) {
            return null;
        }

        return new VariantDeletionTime(value);
    }

    public static @Nullable Instant unwrap(
            @Nullable
            final VariantDeletionTime deletionTime) {
        if (deletionTime == null) {
            return null;
        }

        return deletionTime.value();
    }
}
