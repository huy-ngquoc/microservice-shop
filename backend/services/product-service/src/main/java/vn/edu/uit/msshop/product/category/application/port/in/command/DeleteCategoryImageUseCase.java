package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.DeleteCategoryImageCommand;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;

public interface DeleteCategoryImageUseCase {
  CategoryImageView deleteImage(final DeleteCategoryImageCommand command);
}
