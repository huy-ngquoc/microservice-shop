package vn.uit.edu.msshop.rating.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

public record ProductId(
        UUID value) {
    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException("Id null");
        }
    }

    public static ProductId newId() {
        return new ProductId(UUIDs.newId());
    }
}
