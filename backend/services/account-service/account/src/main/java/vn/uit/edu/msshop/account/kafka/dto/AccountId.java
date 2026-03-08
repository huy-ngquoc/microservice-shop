package vn.uit.edu.msshop.account.kafka.dto;

import java.util.UUID;

public record AccountId(UUID value) { 
    public AccountId {
        if(value==null) {
            throw new IllegalArgumentException("Invalid account id");
        }
    }

}
