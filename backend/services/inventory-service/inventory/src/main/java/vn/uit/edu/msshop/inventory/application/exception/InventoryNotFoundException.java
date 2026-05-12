package vn.uit.edu.msshop.inventory.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import vn.uit.edu.msshop.inventory.domain.model.valueobject.InventoryId;
import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InventoryNotFoundException extends RuntimeException{
    public InventoryNotFoundException(InventoryId inventoryId) {
        super("Inventory not found with id "+inventoryId.value().toString());
    }
    public InventoryNotFoundException(VariantId variantId) {
        super("Inventory not found with id "+variantId.value().toString());
    }
}
 