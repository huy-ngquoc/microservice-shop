package vn.edu.uit.msshop.product.variant.application.exception;

import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public final class VariantNotFoundException extends NotFoundException {
    public VariantNotFoundException(
            final VariantId id,
            final Throwable cause) {
        super(Variant.class.getSimpleName(), id.value().toString(), cause);
    }

    public VariantNotFoundException(
            final VariantId id) {
        super(Variant.class.getSimpleName(), id.value().toString());
    }
}
