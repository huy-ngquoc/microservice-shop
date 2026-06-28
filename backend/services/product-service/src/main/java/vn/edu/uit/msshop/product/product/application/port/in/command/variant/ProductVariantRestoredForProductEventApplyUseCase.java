package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRestoredForProductEventApplyCommand;

public interface ProductVariantRestoredForProductEventApplyUseCase {
    void apply(
            final ProductVariantRestoredForProductEventApplyCommand cmd);
}
