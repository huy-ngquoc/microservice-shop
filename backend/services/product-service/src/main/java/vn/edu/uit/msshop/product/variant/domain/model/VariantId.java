package vn.edu.uit.msshop.product.variant.domain.model;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantId(
        UUID value) {
    public VariantId {
        if (value == null) {
            throw new DomainException("id null");
        }
    }

    public static VariantId newId() {
        return new VariantId(UUID.randomUUID());
    }
}
