package vn.uit.edu.msshop.order.domain.event.inventory;
import java.util.UUID;

public record ForceCancellOrder(UUID orderId) {
    public ForceCancellOrder {
        if(orderId==null) throw new IllegalArgumentException("Invalid order id");
    }

}