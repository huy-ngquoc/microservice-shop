package vn.uit.edu.msshop.inventory.domain.event;

import java.util.UUID;

public record ForceCancellOrder(UUID orderId, UUID eventId) {
    public ForceCancellOrder {
        if(orderId==null) throw new IllegalArgumentException("Invalid order id");
    }

}
