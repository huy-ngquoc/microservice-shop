package vn.uit.edu.payment.domain.model.valueobject;

public record PaymentValue(
        long value) {
    public PaymentValue {
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid payment value");
        }
    }
}
