package vn.uit.edu.msshop.rating.domain.model.valueobject;

import java.util.UUID;

public record RatingId(UUID value) {
    public RatingId{
        if(value==null) {
            throw new IllegalArgumentException("Id null");
        }
    }
    public static RatingId newId() {
        return new RatingId(UUID.randomUUID());
    }
}
