package vn.uit.edu.msshop.order.application.exception;

import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;


public class InventoryNotFoundException extends RuntimeException{
    
    public InventoryNotFoundException(VariantId variantId) {
        super("Inventory not found with id "+variantId.value().toString());
    }
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
 