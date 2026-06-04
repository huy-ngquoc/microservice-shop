package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.ReorderImageCommand;

public interface ReorderImageUseCase {
    public void reOrder(ReorderImageCommand request);
}
