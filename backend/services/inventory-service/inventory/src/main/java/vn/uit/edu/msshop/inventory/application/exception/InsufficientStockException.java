package vn.uit.edu.msshop.inventory.application.exception;

import vn.uit.edu.msshop.inventory.domain.model.valueobject.VariantId;

public class InsufficientStockException extends RuntimeException {
     public InsufficientStockException(VariantId variantId) {
        super(variantId.value().toString()+"Inventory not enough with id ");
    }
    public InsufficientStockException(String message) {
        super(message);
    }
}
