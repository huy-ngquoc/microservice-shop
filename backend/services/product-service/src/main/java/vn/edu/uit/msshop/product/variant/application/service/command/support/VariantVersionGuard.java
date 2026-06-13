package vn.edu.uit.msshop.product.variant.application.service.command.support;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantVersion;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

public final class VariantVersionGuard {

    private VariantVersionGuard() {
    }

    public static void ensureMatch(
            final VariantVersion expected,
            final VariantVersion current) {
        if (!expected.equals(current)) {
            throw new OptimisticLockException(
                    expected.value(),
                    current.value());
        }
    }
}
