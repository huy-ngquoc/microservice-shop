package vn.uit.edu.msshop.order.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDocument {
    private UUID variantId;
    
    private String productName;
    private List<String> traits;
    private String imageKey;
    private UUID productId;
    private int amount;
    private long unitPrice;
}
