package vn.edu.uit.msshop.product.brand.application.port.in.command;

import vn.edu.uit.msshop.product.brand.application.dto.command.SoftDeleteBrandCommand;

public interface SoftDeleteBrandUseCase {
  void delete(final SoftDeleteBrandCommand command);
}
