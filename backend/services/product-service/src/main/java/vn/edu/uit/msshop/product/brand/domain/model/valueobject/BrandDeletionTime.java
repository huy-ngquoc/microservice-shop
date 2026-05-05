package vn.edu.uit.msshop.product.brand.domain.model.valueobject;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.Domains;

public record BrandDeletionTime(
        Instant value) {
    public BrandDeletionTime {
        Domains.requireNonNull(value, "Deletion time value CANNOT be null");
    }

    public static BrandDeletionTime now() {
        return new BrandDeletionTime(Instant.now());
    }

    public static @Nullable BrandDeletionTime ofNullable(
            @Nullable final Instant value) {
        if (value == null) {
            return null;
        }

        return new BrandDeletionTime(value);
    }

    public static @Nullable Instant unwrap(
            @Nullable final BrandDeletionTime deletionTime) {
        if (deletionTime == null) {
            return null;
        }

        return deletionTime.value();
    }
}
