package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductRating(
        float average,
        int count) {
    public static final float MIN_RATING = 0.0F;
    public static final float MAX_RATING = 5.0F;

    private static final ProductRating ZERO = new ProductRating(0.0F, 0);

    public ProductRating {
        if ((average < MIN_RATING) || (average > MAX_RATING)) {
            throw new DomainException("Rating average must be between 0.0 and 5.0");
        }

        if (count < 0) {
            throw new DomainException("Rating count cannot be negative");
        }

        if ((count <= 0) && (average > MIN_RATING)) {
            throw new DomainException("Rating average must be 0 if there are no ratings");
        }
    }

    public static ProductRating zero() {
        return ProductRating.ZERO;
    }
}
