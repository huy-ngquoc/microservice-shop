package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.SoftDeleteCategoryCommand;

public interface SoftDeleteCategoryUseCase {
  void delete(final SoftDeleteCategoryCommand command);
}
