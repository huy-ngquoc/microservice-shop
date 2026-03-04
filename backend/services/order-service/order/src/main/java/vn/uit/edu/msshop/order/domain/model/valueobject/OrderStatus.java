package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.Set;

public record OrderStatus(String value) {
    private static final Set<String> VALID_STATUS = Set.of("PENDING","SHIPPING","RECEIVED","CANCELLED");
    public OrderStatus {
        if(!VALID_STATUS.contains(value)) {
            throw new IllegalArgumentException("Invalid status");
        }
    }

}
