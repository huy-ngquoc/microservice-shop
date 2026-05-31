package vn.uit.edu.msshop.rating.adapter.out.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingCreatedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingDeletedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.dto.integration.RatingUpdatedIntegrationEvent;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingIntegrationEventPort;
import vn.uit.edu.msshop.rating.domain.event.RatingDeleted;
import vn.uit.edu.msshop.rating.domain.event.RatingPosted;
import vn.uit.edu.msshop.rating.domain.event.RatingUpdated;

@Component
@RequiredArgsConstructor
public class RatingIntegrationEventBridge {
    private final PublishRatingIntegrationEventPort integrationPort;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final RatingPosted event) {
        final var msg = new RatingCreatedIntegrationEvent(
                event.getEventId(),
                event.getProductId().value(),
                event.getPoint().value(),
                event.getOccurrenceTime());

        this.integrationPort.publishCreated(msg);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final RatingUpdated event) {
        final var msg = new RatingUpdatedIntegrationEvent(
                event.getEventId(),
                event.getProductId().value(),
                event.getOldPoint().value(),
                event.getNewPoint().value(),
                event.getOccurrenceTime());

        this.integrationPort.publishUpdated(msg);
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT)
    public void on(
            final RatingDeleted event) {
        final var msg = new RatingDeletedIntegrationEvent(
                event.getEventId(),
                event.getProductId().value(),
                event.getPoint().value(),
                event.getOccurrenceTime());

        this.integrationPort.publishDeleted(msg);
    }
}
