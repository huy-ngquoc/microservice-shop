package vn.edu.uit.msshop.product.brand.domain.model;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandLogoKey(
        String value) {
    public static final int MAX_LENGTH = 255;

    public BrandLogoKey {
        if (value == null) {
            throw new DomainException("Key is null");
        }

        if (value.length() > MAX_LENGTH) {
            throw new DomainException("Key is too long");
        }

        if (value.isBlank()) {
            throw new DomainException("Key is blank");
        }

        value = value.trim();
    }

    public static @Nullable BrandLogoKey ofNullable(
            @Nullable
            final String keyString) {
        if (keyString == null) {
            return null;
        }

        return new BrandLogoKey(keyString);
    }

    public static @Nullable String unwrap(
            @Nullable
            final BrandLogoKey key) {
        if (key == null) {
            return null;
        }

        return key.value();
    }
}
