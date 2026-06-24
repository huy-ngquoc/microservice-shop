package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantInfoUpdatedEventApplyCommand;

public interface ProductVariantInfoUpdatedEventApplyUseCase {
    void apply(
            final ProductVariantInfoUpdatedEventApplyCommand cmd);
}
