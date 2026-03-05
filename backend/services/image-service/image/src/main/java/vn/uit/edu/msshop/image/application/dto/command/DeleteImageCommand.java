package vn.uit.edu.msshop.image.application.dto.command;
import vn.uit.edu.msshop.image.domain.model.valueobject.DataType;
import vn.uit.edu.msshop.image.domain.model.valueobject.ImagePublicId;
import vn.uit.edu.msshop.image.domain.model.valueobject.ObjectId;
public record DeleteImageCommand(ImagePublicId publicId, ObjectId objectId, DataType dataType) {

}
