package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;

public interface HardDeleteCategoryUseCase {
    void purge(
            final CategoryLifecycleCommands.HardDelete cmd);
}
