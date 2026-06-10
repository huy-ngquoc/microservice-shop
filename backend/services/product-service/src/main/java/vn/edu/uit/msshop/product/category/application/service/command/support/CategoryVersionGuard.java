package vn.edu.uit.msshop.product.category.application.service.command.support;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryVersion;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

public final class CategoryVersionGuard {

    private CategoryVersionGuard() {
    }

    public static void ensureMatch(
            final CategoryVersion expected,
            final CategoryVersion current) {
        if (!expected.equals(current)) {
            throw new OptimisticLockException(
                    expected.value(),
                    current.value());
        }
    }
}
