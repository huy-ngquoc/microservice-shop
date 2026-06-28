package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.Map;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRestoredForProductEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRestoredForProductEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkIncrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkIncrementPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductVariantRestoredForProductEventApplyService
        implements ProductVariantRestoredForProductEventApplyUseCase {

    private final ProductSoldCountBulkIncrementPort soldCountBulkIncrementPort;
    private final ProductStockCountBulkIncrementPort stockCountBulkIncrementPort;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    public void apply(
            final ProductVariantRestoredForProductEventApplyCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        if (cmd.soldCountIncrement() > 0) {
            final var incrementByProductId = Map.of(productId, cmd.soldCountIncrement());
            this.soldCountBulkIncrementPort.increaseAll(incrementByProductId);
        }
        if (cmd.stockCountIncrement() > 0) {
            final var incrementByProductId = Map.of(productId, cmd.stockCountIncrement());
            this.stockCountBulkIncrementPort.increaseAll(incrementByProductId);
        }
    }

}
