package vn.uit.edu.msshop.image.application.port.in;

import vn.uit.edu.msshop.image.application.dto.command.UploadImageCommand;

public interface UploadImageUseCase {
    public void uploadImage(UploadImageCommand command);
}
