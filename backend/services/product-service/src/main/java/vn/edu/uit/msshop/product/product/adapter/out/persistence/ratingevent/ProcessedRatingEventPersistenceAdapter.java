package vn.edu.uit.msshop.product.product.adapter.out.persistence.ratingevent;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProcessedRatingEventExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProcessedRatingEventCreationPort;

@Component
@RequiredArgsConstructor
class ProcessedRatingEventPersistenceAdapter
        implements
        ProcessedRatingEventExistenceCheckByIdPort,
        ProcessedRatingEventCreationPort {
    private final ProcessedRatingEventMongoRepository repository;

    @Override
    public boolean exists(
            UUID eventId) {
        return this.repository.existsById(eventId);
    }

    @Override
    public void create(
            UUID eventId) {
        final var doc = new ProcessedRatingEventDocument(eventId);
        this.repository.save(doc);
    }
}
