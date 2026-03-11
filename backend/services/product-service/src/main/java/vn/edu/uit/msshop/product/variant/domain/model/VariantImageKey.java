package vn.edu.uit.msshop.product.variant.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantImageKey(
        String value) {
    public VariantImageKey {
        if (value == null) {
            throw new DomainException("Key is null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new DomainException("Key is blank");
        }
    }
}
