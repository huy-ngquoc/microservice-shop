package vn.edu.uit.msshop.product.category.application.port.in.command;

import vn.edu.uit.msshop.product.category.application.dto.command.RestoreCategoryCommand;

public interface RestoreCategoryUseCase {
  void restore(final RestoreCategoryCommand command);
}
