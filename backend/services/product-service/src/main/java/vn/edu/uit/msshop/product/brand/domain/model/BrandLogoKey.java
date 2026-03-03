package vn.edu.uit.msshop.product.brand.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandLogoKey(
        String value) {
    public BrandLogoKey {
        if (value == null) {
            throw new DomainException("publicId null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new DomainException("publicId blank");
        }
    }
}
