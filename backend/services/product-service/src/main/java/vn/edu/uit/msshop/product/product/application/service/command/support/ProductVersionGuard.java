package vn.edu.uit.msshop.product.product.application.service.command.support;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

public final class ProductVersionGuard {

    private ProductVersionGuard() {
    }

    public static void ensureMatch(
            final ProductVersion expected,
            final ProductVersion current) {
        if (!expected.equals(current)) {
            throw new OptimisticLockException(
                    expected.value(),
                    current.value());
        }
    }
}
