package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductPrice(
        long value) {
    private static final ProductPrice ZERO = new ProductPrice(0);

    public ProductPrice {
        if (value < 0) {
            throw new DomainException("Price must not be negative");
        }
    }

    public static ProductPrice zero() {
        return ProductPrice.ZERO;
    }
}
