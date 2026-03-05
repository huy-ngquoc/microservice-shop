package vn.uit.edu.msshop.image.application.port.in;

import vn.uit.edu.msshop.image.application.dto.command.UploadImageCommand;
import vn.uit.edu.msshop.image.domain.model.ImageInfo;

public interface UploadImageUseCase {
    public ImageInfo uploadImage(UploadImageCommand command);
}
