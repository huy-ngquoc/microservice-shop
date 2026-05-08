package vn.uit.edu.msshop.cart.adapter.in.web.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponse {
    private UUID variantId;
    private String productName;
    private List<String> traits;
    private String imageKey;
    private long price;
    private int amount;
    
}
