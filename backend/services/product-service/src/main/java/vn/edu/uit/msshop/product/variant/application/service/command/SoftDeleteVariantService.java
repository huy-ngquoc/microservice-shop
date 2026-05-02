package vn.edu.uit.msshop.product.variant.application.service.command;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.shared.application.exception.OptimisticLockException;
import vn.edu.uit.msshop.product.variant.application.dto.command.SoftDeleteVariantCommand;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.in.command.SoftDeleteVariantUseCase;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantSoldCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantStockCountPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.UpdateVariantPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductSoldCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.DecreaseProductStockCountsPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.RemoveVariantFromProductPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantDeletionTime;

@Service
@RequiredArgsConstructor
public class SoftDeleteVariantService implements SoftDeleteVariantUseCase {
    private final LoadVariantPort loadPort;
    private final LoadVariantSoldCountPort loadSoldCountPort;
    private final LoadVariantStockCountPort loadStockCountPort;
    private final UpdateVariantPort updatePort;
    private final RemoveVariantFromProductPort removeFromProductPort;
    private final DecreaseProductSoldCountsPort decreaseProductSoldPort;
    private final DecreaseProductStockCountsPort decreaseProductStockPort;
    private final PublishVariantEventPort eventPort;

    @Override
    @Transactional
    public void delete(
            final SoftDeleteVariantCommand command) {
        final var variantId = command.id();
        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var expectedVersion = command.expectedVersion();
        final var currentVersion = variant.getVersion();
        if (!expectedVersion.equals(currentVersion)) {
            throw new OptimisticLockException(
                    expectedVersion.value(),
                    currentVersion.value());
        }

        final var productId = variant.getProductId();
        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(variantId, productId);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(variantId, productId);
        final var soldDecrement = soldCount.getValue().value();
        final var stockDecrement = stockCount.getValue().value();

        final var next = new Variant(
                variant.getId(),
                variant.getProductId(),
                variant.getProductName(),
                variant.getPrice(),
                variant.getTraits(),
                variant.getTargets(),
                variant.getImageKey(),
                variant.getVersion(),
                VariantDeletionTime.now());

        final var saved = this.updatePort.update(next);

        this.removeFromProductPort.removeFromProduct(
                saved.getId(),
                saved.getProductId());

        if (soldDecrement > 0) {
            this.decreaseProductSoldPort.decreaseAllSoldCounts(
                    Map.of(productId, soldDecrement));
        }
        if (stockDecrement > 0) {
            this.decreaseProductStockPort.decreaseAllStockCounts(
                    Map.of(productId, stockDecrement));
        }

        this.eventPort.publish(new VariantSoftDeleted(saved.getId()));
    }
}
