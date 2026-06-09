package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategorySoftDeletionByIdCommand;

public interface CategorySoftDeletionByIdUseCase {
    void softDelete(
            final CategorySoftDeletionByIdCommand cmd);
}
