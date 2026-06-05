package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.AddImageCommand;

public interface AddProductImageUseCase {
    public void addImage(AddImageCommand command);
}
