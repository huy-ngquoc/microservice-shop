package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantProductId(
        UUID value) {
    public VariantProductId {
        if (value == null) {
            throw new DomainException("Variant product ID must NOT be null");
        }
    }
}
