package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.UpdateCategoryInfoCommand;

public interface UpdateCategoryInfoUseCase {
    void updateInfo(
            final UpdateCategoryInfoCommand command);
}
