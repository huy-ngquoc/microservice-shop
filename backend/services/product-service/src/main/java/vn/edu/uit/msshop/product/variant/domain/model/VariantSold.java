package vn.edu.uit.msshop.product.variant.domain.model;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantSold(
        int value) {
    private static final VariantSold ZERO = new VariantSold(0);

    public VariantSold {
        if (value < 0) {
            throw new DomainException("Variant sold must NOT be negative");
        }
    }

    public static VariantSold zero() {
        return VariantSold.ZERO;
    }
}
