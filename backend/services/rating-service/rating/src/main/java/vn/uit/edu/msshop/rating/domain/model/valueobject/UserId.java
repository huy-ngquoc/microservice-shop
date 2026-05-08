package vn.uit.edu.msshop.rating.domain.model.valueobject;

import java.util.UUID;

public record UserId(UUID value){
    public UserId{
        if(value==null) {
            throw new IllegalArgumentException("Id null");
        }
    }
    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}
