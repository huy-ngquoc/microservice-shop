package vn.uit.edu.msshop.notification.domain.model.valueobject;

import java.util.UUID;

public record OrderDetail(UUID variantId, String productName, int unitPrice, int amount) {
    public OrderDetail {
        if(variantId==null) throw new IllegalArgumentException("Invalid variant id");
        if(unitPrice<=0) throw new IllegalArgumentException("Invalid unit price");
        if(amount<=0) throw new IllegalArgumentException("Invalid amount");
    }
}
