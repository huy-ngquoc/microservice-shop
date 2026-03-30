package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.List;

public record PaymentMethod(String value) {
    private static final List<String> VALID_PAYMENT_METHOD = List.of("ONLINE","COD");
}
