package vn.uit.edu.msshop.order.adapter.exception;

import java.util.UUID;

public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(UUID variantId) {
        super("Variant does not exist with id "+variantId.toString());
    }
}
