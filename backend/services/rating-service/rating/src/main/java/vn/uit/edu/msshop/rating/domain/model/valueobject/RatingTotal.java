package vn.uit.edu.msshop.rating.domain.model.valueobject;

public record RatingTotal(
        long value) {
    private static final RatingTotal ZERO = new RatingTotal(0);

    public RatingTotal {
        if (value < 0) {
            throw new IllegalArgumentException("Rating total cannot be negative");
        }
    }

    public static RatingTotal zero() {
        return RatingTotal.ZERO;
    }
}
