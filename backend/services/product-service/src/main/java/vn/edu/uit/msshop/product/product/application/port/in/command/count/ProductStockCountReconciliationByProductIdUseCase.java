package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountReconciliationByProductIdCommand;

public interface ProductStockCountReconciliationByProductIdUseCase {
    void reconcile(
            final ProductStockCountReconciliationByProductIdCommand cmd);
}
