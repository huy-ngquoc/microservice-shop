package vn.edu.uit.msshop.product.product.application.port.in;

import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductCommand;

public interface CreateProductUseCase {
    void create(
            final CreateProductCommand command);
}
