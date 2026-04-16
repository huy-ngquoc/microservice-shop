package vn.uit.edu.msshop.rating.domain.model.valueobject;

public record RatingCount(int value) {

    public RatingCount {
        if(value<0) throw new IllegalArgumentException("Invalid rating count");
    }
}
