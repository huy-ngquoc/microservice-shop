package vn.edu.uit.msshop.product.category.domain.model.valueobject;

import org.jspecify.annotations.Nullable;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record CategoryImageKey(
        String value) {
    public static final int MAX_LENGTH = 255;

    public CategoryImageKey {
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

    public static @Nullable CategoryImageKey ofNullable(
            @Nullable final String keyString) {
        if (keyString == null) {
            return null;
        }

        return new CategoryImageKey(keyString);
    }

    public static @Nullable String unwrap(
            @Nullable final CategoryImageKey key) {
        if (key == null) {
            return null;
        }

        return key.value();
    }
}
