package vn.uit.edu.msshop.rating.domain.model.valueobject;
public record RatingPoint(float value) {
    public RatingPoint {
        if(value<0||value>5) {
            throw new IllegalArgumentException("Invalid rating point");
        }
    }
}
