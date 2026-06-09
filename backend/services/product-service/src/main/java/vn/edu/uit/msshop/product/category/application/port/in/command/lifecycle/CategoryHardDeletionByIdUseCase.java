package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;

public interface CategoryHardDeletionByIdUseCase {
    void hardDelete(
            final CategoryLifecycleCommands.HardDelete cmd);
}
