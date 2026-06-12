package vn.edu.uit.msshop.product.variant.application.port.in.command.count;

import vn.edu.uit.msshop.product.variant.application.dto.command.ReconcileVariantStockCountsCommand;

public interface VariantStockCountBulkReconciliationUseCase {
    void execute(
            final ReconcileVariantStockCountsCommand command);
}
