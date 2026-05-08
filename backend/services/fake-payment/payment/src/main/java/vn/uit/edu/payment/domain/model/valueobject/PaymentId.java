package vn.uit.edu.payment.domain.model.valueobject;

import java.util.UUID;

public record PaymentId(
        UUID value) {
    public PaymentId {
        if (value == null) {
            throw new IllegalArgumentException("Invalid payment id");
        }
    }

}
