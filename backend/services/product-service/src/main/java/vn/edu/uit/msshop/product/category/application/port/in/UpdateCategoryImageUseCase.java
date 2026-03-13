package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryImageCommand;

public interface UpdateCategoryImageUseCase {
    void updateImage(
            final UpdateCategoryImageCommand command);
}
