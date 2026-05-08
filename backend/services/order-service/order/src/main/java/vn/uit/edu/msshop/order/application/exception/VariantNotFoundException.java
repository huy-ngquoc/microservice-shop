package vn.uit.edu.msshop.order.application.exception;

import java.util.UUID;

public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(UUID variantId) {
        super("Order not found with id "+variantId.toString());
    }
}
