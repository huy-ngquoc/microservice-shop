package vn.edu.uit.msshop.product.product.application.service.command.variant;

import java.util.Map;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantSoftDeletedForProductEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantSoftDeletedForProductEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductSoldCountBulkDecrementPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.command.ProductStockCountBulkDecrementPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
public class ProductVariantSoftDeletedForProductEventApplyService
        implements ProductVariantSoftDeletedForProductEventApplyUseCase {

    private final ProductSoldCountBulkDecrementPort soldCountBulkDecrementPort;
    private final ProductStockCountBulkDecrementPort stockCountBulkDecrementPort;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    public void apply(
            final ProductVariantSoftDeletedForProductEventApplyCommand cmd) {
        final var productId = new ProductId(cmd.productId());
        if (cmd.soldCountDecrement() > 0) {
            final var decrementByProductId = Map.of(productId, cmd.soldCountDecrement());
            this.soldCountBulkDecrementPort.decreaseAll(decrementByProductId);
        }
        if (cmd.stockCountDecrement() > 0) {
            final var decrementByProductId = Map.of(productId, cmd.stockCountDecrement());
            this.stockCountBulkDecrementPort.decreaseAll(decrementByProductId);
        }
    }
}
