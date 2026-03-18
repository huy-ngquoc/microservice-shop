package vn.uit.edu.msshop.order.domain.event.inventory;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderShipped {
    private UUID orderId;
    private List<OrderDetail> details;
}
