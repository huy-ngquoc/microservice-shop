package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantStockCountValue(
        int value) {
    private static final VariantStockCountValue ZERO = new VariantStockCountValue(0);

    public VariantStockCountValue {
        if (value < 0) {
            throw new DomainException("Variant stock count must NOT be negative");
        }
    }

    public static VariantStockCountValue zero() {
        return VariantStockCountValue.ZERO;
    }
}
