package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;

public interface UpdateCategoryInfoUseCase {
    CategoryView updateInfo(
            final UpdateCategoryInfoCommand command);
}
