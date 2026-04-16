package vn.edu.uit.msshop.product.shared.adapter.out.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mongodb.DuplicateKeyException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.shared.application.port.event.EventIdempotencyPort;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessedEventPersistenceAdapter
        implements EventIdempotencyPort {
    private final ProcessedEventRepository repository;

    @Override
    public boolean tryAcquire(
            final UUID eventId) {
        try {
            final var newEvent = new ProcessedEvent(eventId, Instant.now());
            this.repository.insert(newEvent);
            return true;
        } catch (final DuplicateKeyException _) {
            log.debug("Event ID {} is already existed", eventId);
            return false;
        }
    }
}
