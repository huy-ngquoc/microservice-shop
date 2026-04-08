package vn.uit.edu.msshop.order.application.exception;

import vn.uit.edu.msshop.order.domain.model.valueobject.VariantId;

public class VariantSoldCountNotFoundException extends RuntimeException {
    public VariantSoldCountNotFoundException(VariantId id) {
        super("Variant sold count document not found with id "+id);
    }
}
