package vn.uit.edu.msshop.account.domain.model.valueobject;

import java.util.UUID;

import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

public record AccountId(
        UUID value) {
    public AccountId {
        if (value == null) {
            throw new IllegalArgumentException("Id null");
        }
    }

    public static AccountId newId() {
        return new AccountId(UUIDs.newId());
    }
}
