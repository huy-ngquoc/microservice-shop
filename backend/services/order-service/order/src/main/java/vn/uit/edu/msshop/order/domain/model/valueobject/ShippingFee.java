package vn.uit.edu.msshop.order.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;

public record ShippingFee(long value) { 
    public ShippingFee {
        if(value<0) {
            throw new IllegalArgumentException("Invalid shipping fee");
        }
    }
    @JsonValue
    public long getValue() {
        return this.value;
    }

}
