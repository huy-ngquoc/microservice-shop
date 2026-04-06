package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.HardDeleteCategoryCommand;

public interface HardDeleteCategoryUseCase {
    void purge(
            final HardDeleteCategoryCommand command);
}
