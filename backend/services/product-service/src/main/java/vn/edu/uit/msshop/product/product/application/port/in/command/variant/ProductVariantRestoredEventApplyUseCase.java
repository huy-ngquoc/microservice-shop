package vn.edu.uit.msshop.product.product.application.port.in.command.variant;

import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRestoredEventApplyCommand;

public interface ProductVariantRestoredEventApplyUseCase {
    void apply(
            final ProductVariantRestoredEventApplyCommand cmd);
}
