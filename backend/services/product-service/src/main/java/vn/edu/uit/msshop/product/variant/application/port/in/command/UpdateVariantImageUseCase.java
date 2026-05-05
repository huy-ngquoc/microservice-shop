package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;

public interface UpdateVariantImageUseCase {
  VariantImageView updateImage(final UpdateVariantImageCommand command);
}
