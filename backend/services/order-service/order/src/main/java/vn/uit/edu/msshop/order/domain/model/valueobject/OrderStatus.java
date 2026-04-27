package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonValue;

public record OrderStatus(String value) {
    private static final Set<String> VALID_STATUS = Set.of("PENDING","CONFIRMED","SHIPPING","RECEIVED","CANCELLED");
    public OrderStatus {
        if(!VALID_STATUS.contains(value)) {
            throw new IllegalArgumentException("Invalid status");
        }
    }
    @JsonValue
    public String getValue() {
        return this.value;
    }


}
