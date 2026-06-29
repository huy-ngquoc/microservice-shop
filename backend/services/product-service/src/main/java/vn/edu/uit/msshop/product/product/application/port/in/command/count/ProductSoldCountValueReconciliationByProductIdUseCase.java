package vn.edu.uit.msshop.product.product.application.port.in.command.count;

import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountValueReconciliationByProductIdCommand;

public interface ProductSoldCountValueReconciliationByProductIdUseCase {
    void reconcile(
            final ProductSoldCountValueReconciliationByProductIdCommand cmd);
}
