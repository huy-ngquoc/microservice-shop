package vn.uit.edu.msshop.rating.domain.model.valueobject;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record RatingPoint(
        int value) {
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 5;

    public RatingPoint {
        if (value < MIN_VALUE) {
            throw new DomainException("Rating point must be greater or equal to 1");
        }

        if (value > MAX_VALUE) {
            throw new DomainException("Rating point must be less or equal to 5");
        }
    }
}
