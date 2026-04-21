package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductSoldCountValue(
        int value) {
    private static final ProductSoldCountValue ZERO = new ProductSoldCountValue(0);

    public ProductSoldCountValue {
        if (value < 0) {
            throw new DomainException("Product sold count must NOT be negative");
        }
    }

    public static ProductSoldCountValue zero() {
        return ProductSoldCountValue.ZERO;
    }

    public ProductSoldCountValue increase(
            final int increment) {
        if (increment < 0) {
            throw new DomainException("Product sold count increment cannot be negative");
        }

        final var newValue = this.value + increment;
        return new ProductSoldCountValue(newValue);
    }
}
