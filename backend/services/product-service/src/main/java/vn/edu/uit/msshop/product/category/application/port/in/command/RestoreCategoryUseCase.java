package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;

public interface RestoreCategoryUseCase {
    void restore(
            final CategoryLifecycleCommands.Restore cmd);
}
