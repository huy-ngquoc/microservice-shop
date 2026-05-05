package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductRatingAmount(
        int value) {
    private static final ProductRatingAmount ZERO = new ProductRatingAmount(0);

    public ProductRatingAmount {
        if (value < 0) {
            throw new DomainException("Product rating amount value must NOT be negative");
        }
    }

    public static ProductRatingAmount zero() {
        return ProductRatingAmount.ZERO;
    }
}
