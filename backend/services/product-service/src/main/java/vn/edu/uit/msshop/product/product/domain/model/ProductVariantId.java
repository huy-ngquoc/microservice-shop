package vn.edu.uit.msshop.product.product.domain.model;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariantId(
        UUID value) {
    public ProductVariantId {
        if (value == null) {
            throw new DomainException("Product variant ID is null");
        }
    }

    public static ProductVariantId newId() {
        return new ProductVariantId(UUID.randomUUID());
    }
}
