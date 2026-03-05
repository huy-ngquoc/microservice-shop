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
    private String variantName;
    private String productName;
    private String size;
    private String color;
    private List<String> images;
    private int amount;
    private long unitPrice;
}
