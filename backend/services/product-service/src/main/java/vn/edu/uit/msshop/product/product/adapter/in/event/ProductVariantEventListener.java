package vn.edu.uit.msshop.product.product.adapter.in.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantInfoUpdatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantRestoredEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.dto.command.variant.ProductVariantSoftDeletedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantInfoUpdatedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantRestoredEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.in.command.variant.ProductVariantSoftDeletedEventApplyUseCase;
import vn.edu.uit.msshop.product.variant.domain.event.VariantEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantRestoredEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductVariantEventListener {

    private final ProductVariantInfoUpdatedEventApplyUseCase infoUpdatedEventApplyUseCase;
    private final ProductVariantSoftDeletedEventApplyUseCase softDeletedEventApplyUseCase;
    private final ProductVariantRestoredEventApplyUseCase restoredEventApplyUseCase;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onInfoUpdated(
            final VariantInfoUpdatedEvent event) {
        try {
            final var cmd = new ProductVariantInfoUpdatedEventApplyCommand(
                    event.getEventId(),
                    event.getProductId().value(),
                    event.getVariantId().value(),
                    event.getPrice().value(),
                    event.getTraits().unwrap());
            this.infoUpdatedEventApplyUseCase.apply(cmd);
        } catch (final RuntimeException exception) {
            this.logProjectionFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onSoftDeleted(
            final VariantSoftDeletedEvent event) {
        try {
            final var cmd = new ProductVariantSoftDeletedEventApplyCommand(
                    event.getEventId(),
                    event.getProductId().value(),
                    event.getVariantId().value(),
                    event.getSoldCountValue().value(),
                    event.getStockCountValue().value());
            this.softDeletedEventApplyUseCase.apply(cmd);
        } catch (final RuntimeException exception) {
            this.logProjectionFailure(event, exception);
        }
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onRestored(
            final VariantRestoredEvent event) {
        try {
            final var cmd = new ProductVariantRestoredEventApplyCommand(
                    event.getEventId(),
                    event.getProductId().value(),
                    event.getVariantId().value(),
                    event.getPrice().value(),
                    event.getTraits().unwrap(),
                    event.getSoldCountValue().value(),
                    event.getStockCountValue().value());
            this.restoredEventApplyUseCase.apply(cmd);
        } catch (final RuntimeException exception) {
            this.logProjectionFailure(event, exception);
        }
    }

    private void logProjectionFailure(
            final VariantEvent event,
            final RuntimeException ex) {
        log.error(
                "Variant-to-product projection failed (variant already committed, awaiting rebuild job to converge):"
                        + " event={} product={} variant={}",
                event.getEventId(),
                event.getProductId().value(),
                event.getVariantId().value(),
                ex);
    }
}
