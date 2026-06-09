package vn.edu.uit.msshop.product.category.application.port.in.command.image;

import vn.edu.uit.msshop.product.category.application.dto.command.CategoryImageLifecycleCommands;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;

public interface CategoryImageUpdateByIdUseCase {
    CategoryImageView update(
            final CategoryImageLifecycleCommands.Update cmd);
}
