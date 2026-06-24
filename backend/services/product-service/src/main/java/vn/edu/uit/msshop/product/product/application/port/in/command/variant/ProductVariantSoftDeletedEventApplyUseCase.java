package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantSoftDeletedEventApplyCommand;

public interface ProductVariantSoftDeletedEventApplyUseCase {
    void apply(
            final ProductVariantSoftDeletedEventApplyCommand cmd);
}
