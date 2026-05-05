package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantSoldCountValue(
        int value) {
    private static final VariantSoldCountValue ZERO = new VariantSoldCountValue(0);

    public VariantSoldCountValue {
        if (value < 0) {
            throw new DomainException("Variant sold count must NOT be negative");
        }
    }

    public static VariantSoldCountValue zero() {
        return VariantSoldCountValue.ZERO;
    }
}
