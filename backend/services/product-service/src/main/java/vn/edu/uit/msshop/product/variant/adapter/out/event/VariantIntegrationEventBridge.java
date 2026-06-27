package vn.edu.uit.msshop.product.variant.adapter.out.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantSoftDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantUpdatedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.port.out.event.VariantIntegrationEventPublicationPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeletedForProductEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedEvent;
import vn.edu.uit.msshop.product.variant.domain.event.VariantInfoUpdatedForProductEvent;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@Component
@RequiredArgsConstructor
public class VariantIntegrationEventBridge {

    private final VariantIntegrationEventPublicationPort integrationPort;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final VariantInfoUpdatedEvent event) {
        final var msg = new VariantUpdatedIntegrationEvent(
                UUIDs.newId(),
                event.getVariantId().value(),
                event.getTraits().unwrap(),
                event.getPrice().value(),
                event.getProductName().value(),
                VariantImageKey.unwrap(event.getImageKey()));
        this.integrationPort.publishUpdated(msg);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final VariantSoftDeletedEvent event) {
        final var msg = new VariantSoftDeletedIntegrationEvent(
                UUIDs.newId(),
                event.getVariantId().value());
        this.integrationPort.publishSoftDeleted(msg);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onCascade(
            final VariantInfoUpdatedForProductEvent event) {
        final var msg = new VariantUpdatedIntegrationEvent(
                UUIDs.newId(),
                event.getVariantId().value(),
                event.getTraits().unwrap(),
                event.getPrice().value(),
                event.getProductName().value(),
                VariantImageKey.unwrap(event.getImageKey()));
        this.integrationPort.publishUpdated(msg);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void onCascade(
            final VariantSoftDeletedForProductEvent event) {
        final var msg = new VariantSoftDeletedIntegrationEvent(
                UUIDs.newId(),
                event.getVariantId().value());
        this.integrationPort.publishSoftDeleted(msg);
    }
}
