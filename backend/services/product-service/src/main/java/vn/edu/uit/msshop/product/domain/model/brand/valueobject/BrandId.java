package vn.edu.uit.msshop.product.domain.model.brand.valueobject;

import java.util.UUID;

public record BrandId(
        UUID value) {
    public BrandId {
        if (value == null) {
            throw new IllegalArgumentException("id null");
        }
    }

    public static BrandId newId() {
        return new BrandId(UUID.randomUUID());
    }
}
