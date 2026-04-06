package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public record Currency(String value) {
    private static final List<String> VALID_CURRENCY=List.of("VND","USD");
    public Currency {
        if(!VALID_CURRENCY.contains(value)) throw new IllegalArgumentException("Invalid currency");
    }
    @JsonValue
    public String getValue() {
        return this.value;
    }

}
