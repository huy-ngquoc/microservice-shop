package vn.uit.edu.msshop.rating.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

public record UserId(
        UUID value) {
    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("Id null");
        }
    }

    public static UserId newId() {
        return new UserId(UUIDs.newId());
    }
}
