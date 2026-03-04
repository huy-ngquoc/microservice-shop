package vn.uit.edu.msshop.order.domain.model.valueobject;

import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        if(value==null) {
            throw new IllegalArgumentException("Id can not be null");
        }
    }
}
