package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountValueReconciliationByProductIdCommand;

public interface ProductStockCountValueReconciliationByProductIdUseCase {
    void reconcile(
            final ProductStockCountValueReconciliationByProductIdCommand cmd);
}
