package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariantPrice(
        long value) {
    public ProductVariantPrice {
        if (value < 0) {
            throw new DomainException("Variant price must not be negative");
        }
    }
}
