package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductVariantStockCountBulkUpdatedEventApplyCommand;

public interface ProductVariantStockCountBulkUpdatedEventApplyUseCase {
    void apply(
            final ProductVariantStockCountBulkUpdatedEventApplyCommand cmd);
}
