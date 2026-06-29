package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountSetByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantStockCountSumByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductStockCountValue;

@Service
@RequiredArgsConstructor
class ProductStockCountValueReconciliationByProductIdService
        implements ProductStockCountReconciliationByProductIdUseCase {

    private final ProductVariantStockCountSumByProductIdPort stockCountSumByProductIdPort;
    private final ProductStockCountSetByProductIdPort stockCountSetByProductIdPort;

    @Override
    public void reconcile(
            final ProductStockCountReconciliationByProductIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var sumValue = this.stockCountSumByProductIdPort.sumStockByProductId(productId);
        this.stockCountSetByProductIdPort.setByProductId(
                productId,
                new ProductStockCountValue(sumValue));
    }
}
