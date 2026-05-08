package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonValue;

public record UpdateAt(Instant value) {
    @JsonValue
    public Instant getValue() {
        return this.value;
    }

}
