package vn.edu.uit.msshop.product.product.adapter.out.persistence;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProcessedRatingEventExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CreateProcessedRatingEventPort;

@Component
@RequiredArgsConstructor
public class ProcessedRatingEventPersistenceAdapter
        implements
        CheckProcessedRatingEventExistsPort,
        CreateProcessedRatingEventPort {
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
