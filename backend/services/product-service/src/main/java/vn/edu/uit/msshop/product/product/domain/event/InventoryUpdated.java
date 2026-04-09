package vn.edu.uit.msshop.product.product.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdated {
    private UUID variantId;
    private int newQuantity;
    private int newReservedQuantity;
    private UUID eventId;
}
