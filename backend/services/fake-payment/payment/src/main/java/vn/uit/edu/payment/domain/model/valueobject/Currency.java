package vn.uit.edu.payment.domain.model.valueobject;

import java.util.Set;

public record Currency(
        String value) {
    private static final Set<String> VALID_CURRENCY = Set.of("VND", "USD");

    public Currency {
        if (!VALID_CURRENCY.contains(value)) {
            throw new IllegalArgumentException("Invalid currency");
        }
    }

}
