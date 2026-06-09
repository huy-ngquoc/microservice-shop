package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;

public interface CategoryRestorationByIdUseCase {
    void restore(
            final CategoryLifecycleCommands.Restore cmd);
}
