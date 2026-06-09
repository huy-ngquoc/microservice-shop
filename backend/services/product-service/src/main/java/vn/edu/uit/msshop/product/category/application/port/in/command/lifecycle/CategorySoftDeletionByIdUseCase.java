package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;

public interface CategorySoftDeletionByIdUseCase {
    void softDelete(
            final CategoryLifecycleCommands.SoftDelete cmd);
}
