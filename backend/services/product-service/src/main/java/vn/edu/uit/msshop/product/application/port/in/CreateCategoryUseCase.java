package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.CreateCategoryCommand;

public interface CreateCategoryUseCase {
    void create(
            final CreateCategoryCommand command);
}
