package vn.edu.uit.msshop.product.category.application.port.in.command.image;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryImageLifecycleCommands;

public interface CategoryImageDeletionByIdUseCase {
    void delete(
            final CategoryImageLifecycleCommands.Delete cmd);
}
