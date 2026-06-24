package vn.edu.uit.msshop.product.product.application.service.command.variant;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantInfoUpdatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantUpdateForVariantCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantInfoUpdatedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantUpdateForVariantUseCase;
import vn.edu.uit.msshop.shared.application.exception.OptimisticLockException;

@Service
@RequiredArgsConstructor
class ProductVariantInfoUpdatedEventApplyService
        implements ProductVariantInfoUpdatedEventApplyUseCase {

    private final ProductVariantUpdateForVariantUseCase updateUseCase;

    @Override
    @Transactional
    @Retryable(
            includes = OptimisticLockException.class,
            maxRetries = 3,
            delay = 50,
            multiplier = 2.0,
            maxDelay = 500)
    public void apply(
            final ProductVariantInfoUpdatedEventApplyCommand cmd) {
        final var newCommand = new ProductVariantUpdateForVariantCommand(
                cmd.productId(),
                cmd.variantId(),
                cmd.variantPrice(),
                cmd.variantTraitList());
        this.updateUseCase.update(newCommand);
    }
}
