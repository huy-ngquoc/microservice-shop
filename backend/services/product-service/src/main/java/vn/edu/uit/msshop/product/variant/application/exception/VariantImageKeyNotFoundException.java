package vn.edu.uit.msshop.product.variant.application.exception;

import vn.edu.uit.msshop.product.shared.application.exception.NotFoundException;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;

public final class VariantImageKeyNotFoundException extends NotFoundException {
    public VariantImageKeyNotFoundException(
            final VariantImageKey imageKey,
            final Throwable cause) {
        super("Variant image key", imageKey.value().toString(), cause);
    }

    public VariantImageKeyNotFoundException(
            final VariantImageKey imageKey) {
        super("Variant image key", imageKey.value().toString());
    }
}
