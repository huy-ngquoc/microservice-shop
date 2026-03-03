package vn.edu.uit.msshop.product.brand.domain.model;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record BrandId(
        UUID value) {
    public BrandId {
        if (value == null) {
            throw new DomainException("ID null");
        }
    }

    public static BrandId newId() {
        return new BrandId(UUID.randomUUID());
    }
}
