package vn.edu.uit.msshop.product.variant.domain.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class InventoryUpdated {
    private UUID variantId;
    private int newQuantity;
    private int newReservedQuantity;
    private UUID eventId;
}
