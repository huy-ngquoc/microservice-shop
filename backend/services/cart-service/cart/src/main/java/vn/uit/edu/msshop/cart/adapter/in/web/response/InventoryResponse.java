package vn.uit.edu.msshop.cart.adapter.in.web.response;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private UUID id;
    private UUID variantId;
    private int quantity;
    private int reservedQuantity;
    private Instant updateAt;
}
