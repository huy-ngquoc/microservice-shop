package vn.edu.uit.msshop.product.brand.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandLogoUrl(
        String value) {
    public BrandLogoUrl {
        if (value == null) {
            throw new DomainException("imageUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new DomainException("imageUrl invalid");
        }
    }
}
