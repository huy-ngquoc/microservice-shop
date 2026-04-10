package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantImageKey(
        String value) {
    public static final int MAX_LENGTH = 255;

    public VariantImageKey {
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

    public static @Nullable VariantImageKey ofNullable(
            @Nullable
            final String keyString) {
        if (keyString == null) {
            return null;
        }

        return new VariantImageKey(keyString);
    }

    public static @Nullable String unwrap(
            @Nullable
            final VariantImageKey key) {
        if (key == null) {
            return null;
        }

        return key.value();
    }
}
