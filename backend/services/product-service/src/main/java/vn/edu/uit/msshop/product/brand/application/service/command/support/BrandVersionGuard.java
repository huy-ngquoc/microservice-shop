package vn.edu.uit.msshop.product.brand.application.service.command.support;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

public final class BrandVersionGuard {

    private BrandVersionGuard() {
    }

    public static void ensureMatch(
            final BrandVersion expected,
            final BrandVersion current) {
        if (!expected.equals(current)) {
            throw new OptimisticLockException(expected.value(), current.value());
        }
    }
}
