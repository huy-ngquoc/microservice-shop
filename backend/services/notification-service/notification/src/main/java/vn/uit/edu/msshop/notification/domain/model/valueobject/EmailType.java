package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.Set;

public record EmailType(String value) {
    private static final Set<String> VALID_TYPE = Set.of("ORDER_CREATED","ORDER_RECEIVED","ORDER_SHIPPED","ORDER_CANCELLED","PAYMENT_LINK_CREATED","ONLINE_PAYMENT_SUCCESS","PAYMENT_LINK_EXPIRED","INSUFFICIENT_STOCK", "CANCELLED_BEFORE_PROCESS");
    public EmailType {
        if(!VALID_TYPE.contains(value)) throw new IllegalArgumentException("Invalid email type");
    }
}
