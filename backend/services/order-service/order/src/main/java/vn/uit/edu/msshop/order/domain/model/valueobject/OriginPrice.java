package vn.uit.edu.msshop.order.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;

public record OriginPrice(long value) {
    public OriginPrice {
        if(value<=0) {
            throw new IllegalArgumentException("Invalid origin price");
        }
    }
    @JsonValue
    public long getValue() {
        return this.value;
    }

}
