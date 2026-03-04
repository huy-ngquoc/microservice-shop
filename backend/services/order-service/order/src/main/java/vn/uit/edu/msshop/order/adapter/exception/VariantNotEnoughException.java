package vn.uit.edu.msshop.order.adapter.exception;

import java.util.UUID;

public class VariantNotEnoughException extends RuntimeException {
    public VariantNotEnoughException(UUID variantId) {
        super("Variant with id "+variantId.toString()+" is not enough");
    }
}
