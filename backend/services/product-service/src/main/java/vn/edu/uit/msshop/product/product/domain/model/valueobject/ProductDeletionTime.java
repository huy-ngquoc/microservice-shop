package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.Domains;

public record ProductDeletionTime(
        Instant value) {
    public ProductDeletionTime {
        Domains.requireNonNull(value, "Deletion time value CANNOT be null");
    }

    public static ProductDeletionTime now() {
        return new ProductDeletionTime(Instant.now());
    }

    public static @Nullable ProductDeletionTime ofNullable(
            @Nullable
            final Instant value) {
        if (value == null) {
            return null;
        }

        return new ProductDeletionTime(value);
    }

    public static @Nullable Instant unwrap(
            @Nullable
            final ProductDeletionTime deletionTime) {
        if (deletionTime == null) {
            return null;
        }

        return deletionTime.value();
    }
}
