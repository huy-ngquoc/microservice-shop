package vn.edu.uit.msshop.product.category.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record CategoryImageKey(
        String value) {
    public CategoryImageKey {
        if (value == null) {
            throw new DomainException("publicId null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new DomainException("publicId blank");
        }
    }
}
