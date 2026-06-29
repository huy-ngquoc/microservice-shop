package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountReconciliationByProductIdCommand;

public interface ProductSoldCountReconciliationByProductIdUseCase {
    void reconcile(
            final ProductSoldCountReconciliationByProductIdCommand cmd);
}
