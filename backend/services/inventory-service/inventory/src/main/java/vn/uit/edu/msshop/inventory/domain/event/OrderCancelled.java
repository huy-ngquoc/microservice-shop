package vn.uit.edu.msshop.inventory.domain.event;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelled {
    private UUID orderId;
    private List<OrderDetail> details;
}
