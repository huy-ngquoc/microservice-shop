package vn.uit.edu.msshop.order.domain.event.inventory;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private UUID variantId;
    private int amount;
}
