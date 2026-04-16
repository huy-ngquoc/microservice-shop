package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductSoldCount(
        int value) {
    private static final ProductSoldCount ZERO = new ProductSoldCount(0);

    public ProductSoldCount {
        if (value < 0) {
            throw new DomainException("Product sold count must NOT be negative");
        }
    }

    public static ProductSoldCount zero() {
        return ProductSoldCount.ZERO;
    }

    public ProductSoldCount increase(
            final int increment) {
        if (increment < 0) {
            throw new DomainException("Product sold count increment cannot be negative");
        }

        final var newValue = this.value + increment;
        return new ProductSoldCount(newValue);
    }
}
