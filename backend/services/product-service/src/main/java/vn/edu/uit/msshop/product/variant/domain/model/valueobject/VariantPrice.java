package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantPrice(
        long value) {
    public VariantPrice {
        if (value < 0) {
            throw new DomainException("Variant price must not be negative");
        }
    }
}
