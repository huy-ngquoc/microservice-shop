package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.AddProductVariantForVariantCommand;

public interface AddProductVariantForVariantUseCase {
  void addVariant(final AddProductVariantForVariantCommand command);
}
