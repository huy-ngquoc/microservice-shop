package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantSoldCount(
        int value) {
    private static final VariantSoldCount ZERO = new VariantSoldCount(0);

    public VariantSoldCount {
        if (value < 0) {
            throw new DomainException("Variant sold must NOT be negative");
        }
    }

    public static VariantSoldCount zero() {
        return VariantSoldCount.ZERO;
    }
}
