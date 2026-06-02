package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;

public interface CreateCategoryUseCase {
    CategoryView create(
            final CategoryLifecycleCommands.Create cmd);
}
