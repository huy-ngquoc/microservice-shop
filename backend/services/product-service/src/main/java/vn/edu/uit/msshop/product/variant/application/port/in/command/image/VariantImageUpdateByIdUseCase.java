package vn.edu.uit.msshop.product.variant.application.port.in.command.image;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;

public interface VariantImageUpdateByIdUseCase {
    VariantImageView updateImage(
            final UpdateVariantImageCommand command);
}
