package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariant(
        ProductVariantId id,
        ProductVariantPrice price,
        ProductVariantTraits traits) {
    public ProductVariant {
        if (id == null) {
            throw new DomainException("Variant ID cannot be null in summary");
        }

        if (price == null) {
            throw new DomainException("Variant price cannot be null in summary");
        }

        if (traits == null) {
            throw new DomainException("Variant traits cannot be null in summary");
        }
    }
}
