package vn.uit.edu.msshop.order.domain.model.valueobject;

import com.fasterxml.jackson.annotation.JsonValue;

public record TotalPrice(long value) { 
    public TotalPrice{
        if(value<0) {
            throw new IllegalArgumentException("Invalid total price");
        }
    }
    @JsonValue
    public long getValue() {
        return this.value;
    }


}
