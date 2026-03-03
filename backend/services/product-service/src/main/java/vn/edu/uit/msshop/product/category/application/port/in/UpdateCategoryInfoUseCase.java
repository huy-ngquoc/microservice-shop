package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryInfoCommand;

public interface UpdateCategoryInfoUseCase {
    void updateInfo(
            final UpdateCategoryInfoCommand command);
}
