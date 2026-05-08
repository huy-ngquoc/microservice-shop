package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.valueobject.Media;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

public interface UploadRatingImagePort {
    public Media upload(RatingId ratingId, byte[] bytes,String originalFileName, String contentType);
}
