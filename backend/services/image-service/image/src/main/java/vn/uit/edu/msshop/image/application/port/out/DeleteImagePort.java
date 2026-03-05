package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;

public interface DeleteImagePort {
    public void deleteImage(ImagePublicId pulblicId, ObjectId objectId, DataType dataType);
}
