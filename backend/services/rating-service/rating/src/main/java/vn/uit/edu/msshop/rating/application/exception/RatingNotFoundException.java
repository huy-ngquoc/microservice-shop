package vn.uit.edu.msshop.rating.application.exception;

import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(RatingId id) {
        super("Rating with id "+id.toString()+" does not exist");
    }
}
