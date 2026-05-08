package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.RatingInfo;

public interface SaveRatingInfoPort {
    public RatingInfo save(RatingInfo ratingInfo);
}
