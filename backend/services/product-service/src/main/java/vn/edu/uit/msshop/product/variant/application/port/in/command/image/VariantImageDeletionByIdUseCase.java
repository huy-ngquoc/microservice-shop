package vn.edu.uit.msshop.product.variant.application.port.in.command.image;

import vn.edu.uit.msshop.product.variant.application.dto.command.image.VariantImageDeletionByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;

public interface VariantImageDeletionByIdUseCase {
    VariantImageView deleteImage(
            final VariantImageDeletionByIdCommand cmd);
}
