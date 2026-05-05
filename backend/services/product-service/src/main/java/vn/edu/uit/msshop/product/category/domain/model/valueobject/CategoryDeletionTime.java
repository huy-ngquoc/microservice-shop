package vn.edu.uit.msshop.product.category.domain.model.valueobject;

import java.time.Instant;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.Domains;

public record CategoryDeletionTime(
        Instant value) {
    public CategoryDeletionTime {
        Domains.requireNonNull(value, "Deletion time value CANNOT be null");
    }

    public static CategoryDeletionTime now() {
        return new CategoryDeletionTime(Instant.now());
    }

    public static @Nullable CategoryDeletionTime ofNullable(
            @Nullable
            final Instant value) {
        if (value == null) {
            return null;
        }

        return new CategoryDeletionTime(value);
    }

    public static @Nullable Instant unwrap(
            @Nullable
            final CategoryDeletionTime deletionTime) {
        if (deletionTime == null) {
            return null;
        }

        return deletionTime.value();
    }
}
