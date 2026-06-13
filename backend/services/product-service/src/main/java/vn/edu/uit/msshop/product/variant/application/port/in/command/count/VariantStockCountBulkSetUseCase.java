package vn.edu.uit.msshop.product.variant.application.port.in.command.count;

import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkSetCommand;

public interface VariantStockCountBulkSetUseCase {
    void execute(
            final VariantStockCountBulkSetCommand cmd);
}
