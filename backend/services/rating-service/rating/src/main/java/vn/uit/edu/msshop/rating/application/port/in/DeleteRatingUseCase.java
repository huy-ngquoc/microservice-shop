package vn.uit.edu.msshop.rating.application.port.in;

import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

public interface DeleteRatingUseCase {
    public void delete(RatingId id);
}
