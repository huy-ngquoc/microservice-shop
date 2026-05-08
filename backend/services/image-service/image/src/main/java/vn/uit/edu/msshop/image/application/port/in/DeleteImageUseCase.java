package vn.uit.edu.msshop.image.application.port.in;

import vn.uit.edu.msshop.image.application.dto.command.DeleteImageCommand;

public interface DeleteImageUseCase {
    public void deleteImage(DeleteImageCommand command);
}
