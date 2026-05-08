package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.Set;

public record PaymentStatus(String value) {
    private static final Set<String> VALID_PAYMENT_STATUS = Set.of("PENDING","SUCCESS","FAILED","CANCELLED","EXPIRED","ERROR");
    public PaymentStatus {
        if(!VALID_PAYMENT_STATUS.contains(value)) {
            throw new IllegalArgumentException("Invalid payment status");
        }
    }
}
