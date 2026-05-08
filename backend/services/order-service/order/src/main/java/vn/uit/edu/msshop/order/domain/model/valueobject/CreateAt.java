package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonValue;

public record CreateAt(Instant value) {
    public CreateAt {
        if(value==null) {
            throw new IllegalArgumentException("Invalid creation time");
        }
    }
    @JsonValue
    public Instant getValue() {
        return this.value;
    }

}
