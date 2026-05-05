package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductRatingAverage(
        float value) {
    public static final float MIN_VALUE = 0;
    public static final float MAX_VALUE = 5;

    private static final ProductRatingAverage ZERO = new ProductRatingAverage(0);

    public ProductRatingAverage {
        if (value < MIN_VALUE) {
            throw new DomainException("Rating average must NOT be negative");
        }

        if (value > MAX_VALUE) {
            throw new DomainException("Rating average must NOT be greater than " + MAX_VALUE);
        }
    }

    public static ProductRatingAverage zero() {
        return ProductRatingAverage.ZERO;
    }
}
