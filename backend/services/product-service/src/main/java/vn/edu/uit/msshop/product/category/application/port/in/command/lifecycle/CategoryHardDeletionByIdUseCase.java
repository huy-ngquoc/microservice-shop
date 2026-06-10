package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryHardDeletionByIdCommand;

public interface CategoryHardDeletionByIdUseCase {
    void hardDelete(
            final CategoryHardDeletionByIdCommand cmd);
}
