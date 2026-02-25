package vn.edu.uit.msshop.product.domain.model.category.valueobject;

import java.util.UUID;

public record CategoryId(
        UUID value) {
    public CategoryId {
        if (value == null) {
            throw new IllegalArgumentException("id null");
        }
    }

    public static CategoryId newId() {
        return new CategoryId(UUID.randomUUID());
    }
}
