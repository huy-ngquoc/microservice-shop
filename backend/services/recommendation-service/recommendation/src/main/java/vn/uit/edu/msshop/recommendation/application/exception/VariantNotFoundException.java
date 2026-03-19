package vn.uit.edu.msshop.recommendation.application.exception;

import vn.uit.edu.msshop.recommendation.domain.model.valueobject.VariantId;

public class VariantNotFoundException extends RuntimeException {
    public VariantNotFoundException(VariantId id) {
        super("Variant not found with id "+id.value().toString());
    }
}
