package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteImageCommand;

public interface DeleteImageUseCase {
    public void deleteImage(DeleteImageCommand command);
}
