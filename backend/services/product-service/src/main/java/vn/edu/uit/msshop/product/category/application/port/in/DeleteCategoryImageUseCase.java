package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.command.DeleteCategoryImageCommand;

public interface DeleteCategoryImageUseCase {
    void deleteImage(
            final DeleteCategoryImageCommand command);
}
