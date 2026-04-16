package vn.uit.edu.msshop.rating.application.exception;

import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;

public class RatingInfoNotFoundException extends RuntimeException {
    public RatingInfoNotFoundException(ProductId id) {
        super("Rating info with id "+id.toString()+" does not exist");
    }
}
