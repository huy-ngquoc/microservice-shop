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

    public VariantSoldCount increase(
            final int increment) {
        if (increment < 0) {
            throw new DomainException("Variant sold count increment cannot be negative");
        }

        final var newValue = this.value + increment;
        return new VariantSoldCount(newValue);
    }
}
