package vn.uit.edu.msshop.image.application.port.out;

import vn.uit.edu.msshop.image.domain.model.ImageInfo;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;

public interface UploadImagePort {
    public ImageInfo upload(ImageFileName imageFileName,ObjectId objectId, DataType dataType,byte[] bytes);
}
