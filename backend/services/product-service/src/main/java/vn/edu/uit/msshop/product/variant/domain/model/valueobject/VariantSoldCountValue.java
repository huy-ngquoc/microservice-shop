package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantSoldCountValue(
        int value) {
    private static final VariantSoldCountValue ZERO = new VariantSoldCountValue(0);

    public VariantSoldCountValue {
        if (value < 0) {
            throw new DomainException("Variant sold must NOT be negative");
        }
    }

    public static VariantSoldCountValue zero() {
        return VariantSoldCountValue.ZERO;
    }

    public VariantSoldCountValue increase(
            final int increment) {
        if (increment < 0) {
            throw new DomainException("Variant sold count increment cannot be negative");
        }

        final var newValue = this.value + increment;
        return new VariantSoldCountValue(newValue);
    }
}
