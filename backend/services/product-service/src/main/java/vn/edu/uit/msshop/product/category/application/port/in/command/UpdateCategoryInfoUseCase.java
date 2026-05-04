package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;

public interface UpdateCategoryInfoUseCase {
    CategoryView updateInfo(
            final UpdateCategoryInfoCommand command);
}
