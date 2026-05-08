package vn.uit.edu.payment.domain.model.valueobject;

import java.util.Set;

public record PaymentMethod(
        String value) {
    private static final Set<String> VALID_PAYMENT_METHOD = Set.of("COD", "ONLINE");

    public PaymentMethod {
        if (!VALID_PAYMENT_METHOD.contains(value)) {
            throw new IllegalArgumentException("Invalid payment method");
        }
    }

}
