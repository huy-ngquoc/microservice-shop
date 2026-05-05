package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.UpdateCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;

public interface UpdateCategoryImageUseCase {
  CategoryImageView updateImage(final UpdateCategoryImageCommand command);
}
