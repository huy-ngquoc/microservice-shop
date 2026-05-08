package vn.uit.edu.msshop.rating.application.port.out;

import vn.uit.edu.msshop.rating.domain.model.valueobject.MediaPublicId;

public interface DeleteRatingImagePort {
    public void deleteRatingImage(MediaPublicId publicId);
}
