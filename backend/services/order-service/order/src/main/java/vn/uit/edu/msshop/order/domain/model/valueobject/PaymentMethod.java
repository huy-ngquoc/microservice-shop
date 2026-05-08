package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

public record PaymentMethod(String value) {
    private static final List<String> VALID_PAYMENT_METHOD = List.of("ONLINE","COD");
    public PaymentMethod {
        if(!VALID_PAYMENT_METHOD.contains(value)) throw new IllegalArgumentException("Invalid payment method");
    }
    @JsonValue
    public String getValue() {
        return this.value;
    }

}
