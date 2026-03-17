package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;

public interface UpdateVariantImageUseCase {
    VariantImageView updateImage(
            final UpdateVariantImageCommand command);
}
