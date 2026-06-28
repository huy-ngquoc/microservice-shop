package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantSoftDeletedForProductEventApplyCommand;

public interface ProductVariantSoftDeletedForProductEventApplyUseCase {
    void apply(
            final ProductVariantSoftDeletedForProductEventApplyCommand cmd);
}
