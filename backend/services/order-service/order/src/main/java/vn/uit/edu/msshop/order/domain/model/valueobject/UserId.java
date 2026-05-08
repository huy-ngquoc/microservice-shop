package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonValue;

public record UserId(UUID value) {
    public UserId {
        if(value==null) {
            throw new IllegalArgumentException("Invalid user id");
        }
    }
    @JsonValue
    public UUID getValue() {
        return this.value;
    }

}
