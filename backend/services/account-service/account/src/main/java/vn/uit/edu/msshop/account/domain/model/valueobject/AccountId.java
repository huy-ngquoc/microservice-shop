package vn.uit.edu.msshop.account.domain.model.valueobject;

import java.util.UUID;

public record AccountId(UUID value) {
    public AccountId{
        if(value==null) {
            throw new IllegalArgumentException("Id null");
        }
    }
    public static AccountId newId() {
        return new AccountId(UUID.randomUUID());
    }
}
