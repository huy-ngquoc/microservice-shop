package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.UpdateProductVariantForVariantCommand;

public interface UpdateProductVariantForVariantUseCase {
  void updateVariant(final UpdateProductVariantForVariantCommand command);
}
