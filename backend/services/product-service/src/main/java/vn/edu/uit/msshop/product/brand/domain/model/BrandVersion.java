package vn.edu.uit.msshop.product.brand.domain.model;

import org.jspecify.annotations.Nullable;

public record BrandVersion(
        long value) {
    public static @Nullable BrandVersion ofNullable(
            @Nullable
            final Long rawValue) {
        if (rawValue == null) {
            return null;
        }

        return new BrandVersion(rawValue);
    }

    public static @Nullable Long unwrap(
            @Nullable
            final BrandVersion version) {
        if (version == null) {
            return null;
        }

        return version.value();
    }
}
