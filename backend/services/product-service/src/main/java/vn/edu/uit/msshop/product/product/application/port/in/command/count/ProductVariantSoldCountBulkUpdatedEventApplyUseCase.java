package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductVariantSoldCountBulkUpdatedEventApplyCommand;

public interface ProductVariantSoldCountBulkUpdatedEventApplyUseCase {
    void apply(
            final ProductVariantSoldCountBulkUpdatedEventApplyCommand cmd);
}
