package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductId(
        UUID value) {
    public ProductId {
        if (value == null) {
            throw new DomainException("id null");
        }
    }

    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}
