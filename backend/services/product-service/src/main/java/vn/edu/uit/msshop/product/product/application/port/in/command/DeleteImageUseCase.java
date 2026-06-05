package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.DeleteProductImageCommand;

public interface DeleteImageUseCase {
    public void deleteImage(DeleteProductImageCommand command);
}
