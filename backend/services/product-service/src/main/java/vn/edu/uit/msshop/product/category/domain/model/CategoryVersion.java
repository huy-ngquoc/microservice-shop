package vn.edu.uit.msshop.product.category.domain.model;

import org.jspecify.annotations.Nullable;

public record CategoryVersion(
        long value) {
    public static @Nullable CategoryVersion ofNullable(
            @Nullable
            final Long rawValue) {
        if (rawValue == null) {
            return null;
        }

        return new CategoryVersion(rawValue);
    }

    public static @Nullable Long unwrap(
            @Nullable
            final CategoryVersion version) {
        if (version == null) {
            return null;
        }

        return version.value();
    }
}
