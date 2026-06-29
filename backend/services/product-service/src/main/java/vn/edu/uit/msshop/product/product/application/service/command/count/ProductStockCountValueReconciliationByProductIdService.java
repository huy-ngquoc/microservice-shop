package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductStockCountValueReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductStockCountValueReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountValueSetByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantStockCountValueSumByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductStockCountValue;

@Service
@RequiredArgsConstructor
class ProductStockCountValueReconciliationByProductIdService
        implements ProductStockCountValueReconciliationByProductIdUseCase {

    private final ProductVariantStockCountValueSumByProductIdPort stockCountValueSumByProductIdPort;
    private final ProductStockCountValueSetByProductIdPort stockCountValueSetByProductIdPort;

    @Override
    public void reconcile(
            final ProductStockCountValueReconciliationByProductIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var sumValue = this.stockCountValueSumByProductIdPort.sumStockByProductId(productId);
        this.stockCountValueSetByProductIdPort.setByProductId(
                productId,
                new ProductStockCountValue(sumValue));
    }
}
