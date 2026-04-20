package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.Set;

public record OrderCurrency(String value) {
    private static final Set<String> VALID_CURRENCY = Set.of("VND","USD");
    public OrderCurrency {
        if(!VALID_CURRENCY.contains(value)) throw new IllegalArgumentException("Invalid currency");
    }

}
