package vn.edu.uit.msshop.product.product.application.service.command.variant;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRemovalForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantSoftDeletedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRemovalForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantSoftDeletedEventApplyUseCase;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
class ProductVariantSoftDeletedEventApplyService
        implements ProductVariantSoftDeletedEventApplyUseCase {

    private final ProductVariantRemovalForVariantUseCase removalUseCase;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    public void apply(
            final ProductVariantSoftDeletedEventApplyCommand cmd) {
        final var newCommand = new ProductVariantRemovalForVariantCommand(
                cmd.productId(),
                cmd.variantId(),
                cmd.productSoldCountDecrement(),
                cmd.productStockCountDecrement());
        this.removalUseCase.remove(newCommand);
    }

}
