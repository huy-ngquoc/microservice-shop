package vn.uit.edu.msshop.inventory.application.exception;

import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(InventoryId inventoryId) {
        super("Inventory not found with id "+inventoryId.value().toString());
    }
    public InventoryNotFoundException(VariantId variantId) {
        super("Inventory not found with id "+variantId.value().toString());
    }
}
 