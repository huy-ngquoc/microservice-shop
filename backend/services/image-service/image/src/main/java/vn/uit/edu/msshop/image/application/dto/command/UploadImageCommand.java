package vn.uit.edu.msshop.image.application.dto.command;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImageFileName;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;

public record UploadImageCommand(ImageFileName fileName, ObjectId objectId, DataType dataType,byte[] bytes) {

}
