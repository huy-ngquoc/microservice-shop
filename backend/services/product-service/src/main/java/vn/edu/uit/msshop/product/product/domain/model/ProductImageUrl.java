package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductImageUrl(
        String value) {
    public ProductImageUrl {
        if (value == null) {
            throw new DomainException("imageUrl null");
        }

        value = value.trim();

        if (!value.startsWith("http")) {
            throw new DomainException("imageUrl invalid");
        }
    }
}
