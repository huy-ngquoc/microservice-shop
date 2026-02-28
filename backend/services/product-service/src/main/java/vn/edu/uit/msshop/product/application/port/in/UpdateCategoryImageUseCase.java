package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.application.dto.query.CategoryImageView;

public interface UpdateCategoryImageUseCase {
    CategoryImageView updateImage(
            final UpdateCategoryImageCommand command);
}
