package vn.uit.edu.msshop.order.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailEvent {
    private UUID variantId;
    
    
    private UUID productId;
    private int amount;
    
}
