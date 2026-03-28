package vn.uit.edu.msshop.auth.domain.event;

import java.util.UUID;

public record AccountId(UUID eventId,UUID value) {
     public AccountId {
        if(value==null) {
            throw new IllegalArgumentException("Invalid account id");
        }
    }
}
