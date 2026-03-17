package vn.uit.edu.msshop.inventory.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdated {
    private UUID variantId;
    private int newQuantity;
    private int newReservedQuantity;
}
