package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;

public interface DeleteImagePort {
    public void deleteImage(ImagePublicId pulblicId);
}
