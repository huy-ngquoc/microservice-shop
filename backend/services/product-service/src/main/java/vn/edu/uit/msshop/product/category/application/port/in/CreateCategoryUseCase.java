package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;

public interface CreateCategoryUseCase {
    void create(
            final CreateCategoryCommand command);
}
