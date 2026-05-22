package vn.uit.edu.msshop.rating.domain.model.valueobject;

public record RatingCount(
        long value) {
    private static final RatingCount ZERO = new RatingCount(0);
    private static final RatingCount ONE = new RatingCount(1);

    public RatingCount {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid rating count");
        }
    }

    public static RatingCount zero() {
        return RatingCount.ZERO;
    }

    public static RatingCount one() {
        return RatingCount.ONE;
    }
}
