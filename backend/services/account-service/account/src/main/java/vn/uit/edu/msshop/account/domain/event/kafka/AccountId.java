package vn.uit.edu.msshop.account.domain.event.kafka;

import java.util.UUID;

public record AccountId(UUID value, UUID eventId) { 
    public AccountId {
        if(value==null) {
            throw new IllegalArgumentException("Invalid account id");
        }
    }

}
