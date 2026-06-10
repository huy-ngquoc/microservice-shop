package vn.edu.uit.msshop.product.category.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.category.application.dto.command.lifecycle.CategoryInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;

public interface CategoryInfoUpdateByIdUseCase {
    CategoryView updateInfo(
            final CategoryInfoUpdateByIdCommand cmd);
}
