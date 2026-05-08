package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonValue;

public record OrderId(UUID value) {
    public OrderId {
        if(value==null) {
            throw new IllegalArgumentException("Id can not be null");
        }
    }
    @JsonValue
    public UUID getValue() {
        return this.value;
    }

}
