package vn.uit.edu.msshop.order.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;

public record Discount(long value) {
    public Discount {
        if(value<0) {
            throw new IllegalArgumentException("Invalid discount");
        }
    }
    @JsonValue
    public long getValue() {
        return this.value;
    }

}
