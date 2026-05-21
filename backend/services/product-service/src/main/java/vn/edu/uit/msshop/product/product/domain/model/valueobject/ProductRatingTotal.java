package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductRatingTotal(
        long value) {
    private static final ProductRatingTotal ZERO = new ProductRatingTotal(0);

    public ProductRatingTotal {
        if (value < 0) {
            throw new DomainException("Rating total must NOT be negative");
        }
    }

    public static ProductRatingTotal zero() {
        return ProductRatingTotal.ZERO;
    }
}
