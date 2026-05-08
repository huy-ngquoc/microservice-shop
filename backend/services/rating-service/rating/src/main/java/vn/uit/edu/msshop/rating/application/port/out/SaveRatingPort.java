package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.Rating;

public interface SaveRatingPort {
    public Rating save(Rating rating);
}
