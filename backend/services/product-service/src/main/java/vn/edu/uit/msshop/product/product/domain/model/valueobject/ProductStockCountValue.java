package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductStockCountValue(
        int value) {
    private static final ProductStockCountValue ZERO = new ProductStockCountValue(0);

    public ProductStockCountValue {
        if (value < 0) {
            throw new DomainException("Product stock count must NOT be negative");
        }
    }

    public static ProductStockCountValue zero() {
        return ProductStockCountValue.ZERO;
    }

    public ProductStockCountValue increase(
            final int increment) {
        if (increment < 0) {
            throw new DomainException("Product stock count increment cannot be negative");
        }

        final var newValue = this.value + increment;
        return new ProductStockCountValue(newValue);
    }
}
