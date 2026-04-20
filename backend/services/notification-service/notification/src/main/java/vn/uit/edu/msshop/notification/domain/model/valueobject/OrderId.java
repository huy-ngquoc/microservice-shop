package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        if(value==null) throw new IllegalArgumentException("Invalid order id");
    }

}
