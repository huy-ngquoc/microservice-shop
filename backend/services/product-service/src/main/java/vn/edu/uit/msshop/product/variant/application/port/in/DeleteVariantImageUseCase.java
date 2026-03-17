package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.command.DeleteVariantImageCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantImageView;

public interface DeleteVariantImageUseCase {
    VariantImageView deleteImage(
            final DeleteVariantImageCommand command);
}
