package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.command.CreateCategoryCommand;
import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;

public interface CreateCategoryUseCase {
    CategoryView create(
            final CreateCategoryCommand command);
}
