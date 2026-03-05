package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductImageKey(
        String value) {
    public ProductImageKey {
        if (value == null) {
            throw new DomainException("Key is null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new DomainException("Key is blank");
        }
    }
}
