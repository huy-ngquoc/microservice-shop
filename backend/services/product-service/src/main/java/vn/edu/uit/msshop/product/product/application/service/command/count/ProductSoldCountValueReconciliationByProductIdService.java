package vn.edu.uit.msshop.product.product.application.service.command.count;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.count.ProductSoldCountValueReconciliationByProductIdCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.count.ProductSoldCountValueReconciliationByProductIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountValueSetByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantSoldCountValueSumByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductSoldCountValue;

@Service
@RequiredArgsConstructor
class ProductSoldCountValueReconciliationByProductIdService
        implements ProductSoldCountValueReconciliationByProductIdUseCase {

    private final ProductVariantSoldCountValueSumByProductIdPort soldCountValueSumByProductIdPort;
    private final ProductSoldCountValueSetByProductIdPort soldCountValueSetByProductIdPort;

    @Override
    public void reconcile(
            final ProductSoldCountValueReconciliationByProductIdCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        final var sumValue = this.soldCountValueSumByProductIdPort.sumSoldByProductId(productId);
        this.soldCountValueSetByProductIdPort.setByProductId(
                productId,
                new ProductSoldCountValue(sumValue));
    }
}
