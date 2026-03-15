package vn.edu.uit.msshop.product.category.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

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
}
