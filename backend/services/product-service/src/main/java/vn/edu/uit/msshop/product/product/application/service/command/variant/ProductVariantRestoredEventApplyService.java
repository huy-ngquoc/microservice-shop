package vn.edu.uit.msshop.product.product.application.service.command.variant;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantAdditionForVariantCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRestoredEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantAdditionForVariantUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRestoredEventApplyUseCase;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
class ProductVariantRestoredEventApplyService
        implements ProductVariantRestoredEventApplyUseCase {

    private final ProductVariantAdditionForVariantUseCase additionUseCase;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    public void apply(
            final ProductVariantRestoredEventApplyCommand cmd) {
        final var newCommand = new ProductVariantAdditionForVariantCommand(
                cmd.productId(),
                cmd.variantId(),
                cmd.variantPrice(),
                cmd.variantTraitList(),
                cmd.productSoldCountIncrement(),
                cmd.productStockCountIncrement());
        this.additionUseCase.add(newCommand);
    }

}
