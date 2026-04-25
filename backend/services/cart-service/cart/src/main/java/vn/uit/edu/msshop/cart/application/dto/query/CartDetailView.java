package vn.uit.edu.msshop.cart.application.dto.query;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDetailView {
    private UUID variantId;
    private List<String> traits;
    private String imageKey;
    private String name;
    private long price;
    private int amount;
    private int inventoryAmount;
}
