package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountSetByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantSoldCountSumByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCountValue;

@Service
@RequiredArgsConstructor
class ProductSoldCountReconciliationByProductIdService
        implements ProductSoldCountReconciliationByProductIdUseCase {

    private final ProductVariantSoldCountSumByProductIdPort soldCountSumByProductIdPort;
    private final ProductSoldCountSetByProductIdPort soldCountSetByProductIdPort;

    @Override
    public void reconcile(
            final ProductSoldCountReconciliationByProductIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var sumValue = this.soldCountSumByProductIdPort.sumSoldByProductId(productId);
        this.soldCountSetByProductIdPort.setByProductId(
                productId,
                new ProductSoldCountValue(sumValue));
    }
}
