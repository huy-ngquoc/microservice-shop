package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryRestorationByIdCommand;

public interface CategoryRestorationByIdUseCase {
    void restore(
            final CategoryRestorationByIdCommand cmd);
}
