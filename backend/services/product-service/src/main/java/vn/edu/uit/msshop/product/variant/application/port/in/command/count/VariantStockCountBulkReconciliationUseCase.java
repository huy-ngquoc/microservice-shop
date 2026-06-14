package vn.edu.uit.msshop.product.variant.application.port.in.command.count;

import vn.edu.uit.msshop.product.variant.application.dto.command.count.VariantStockCountBulkReconciliationCommand;

public interface VariantStockCountBulkReconciliationUseCase {
    void reconcileAll(
            final VariantStockCountBulkReconciliationCommand cmd);
}
