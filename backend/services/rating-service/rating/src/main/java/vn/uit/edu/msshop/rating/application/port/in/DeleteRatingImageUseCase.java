package vn.uit.edu.msshop.rating.application.port.in;

import vn.uit.edu.msshop.rating.domain.model.valueobject.MediaPublicId;

public interface DeleteRatingImageUseCase {
    public void delete(MediaPublicId id);

}
