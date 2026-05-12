package vn.edu.uit.msshop.product.variant.adapter.out.event;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantSoftDeletedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.dto.integration.VariantUpdatedIntegrationEvent;
import vn.edu.uit.msshop.product.variant.application.exception.VariantNotFoundException;
import vn.edu.uit.msshop.product.variant.application.port.out.event.PublishVariantIntegrationEventPort;
import vn.edu.uit.msshop.product.variant.application.port.out.persistence.LoadVariantPort;
import vn.edu.uit.msshop.product.variant.domain.event.VariantSoftDeleted;
import vn.edu.uit.msshop.product.variant.domain.event.VariantUpdated;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;
import vn.edu.uit.msshop.shared.domain.identifier.UUIDs;

@Component
@RequiredArgsConstructor
public class VariantIntegrationEventBridge {
    private final LoadVariantPort loadPort;
    private final PublishVariantIntegrationEventPort integrationPort;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final VariantUpdated event) {
        final var variantId = event.getVariantId();
        final var variant = this.loadPort.loadById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        final var msg = new VariantUpdatedIntegrationEvent(
                UUIDs.newId(),
                variant.getId().value(),
                variant.getTraits().unwrap(), variant.getPrice().value(),
                variant.getProductName().value(),
                VariantImageKey.unwrap(variant.getImageKey()));

        this.integrationPort.publishUpdated(msg);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final VariantSoftDeleted event) {
        final var msg = new VariantSoftDeletedIntegrationEvent(
                UUIDs.newId(),
                event.getVariantId().value());
        this.integrationPort.publishSoftDeleted(msg);
    }
}
