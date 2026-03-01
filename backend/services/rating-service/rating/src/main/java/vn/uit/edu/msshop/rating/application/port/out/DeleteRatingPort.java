package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

public interface DeleteRatingPort {
    public void deleteById(RatingId id);
}
