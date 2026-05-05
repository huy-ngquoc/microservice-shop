package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;

public interface DeleteVariantImageUseCase {
  VariantImageView deleteImage(final DeleteVariantImageCommand command);
}
