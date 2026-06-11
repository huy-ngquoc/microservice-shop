package vn.edu.uit.msshop.product.product.adapter.out.persistence.ratingevent;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProductProcessedRatingEventExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProductProcessedRatingEventCreationPort;

@Component
@RequiredArgsConstructor
class ProductProcessedRatingEventPersistenceAdapter
        implements
        ProductProcessedRatingEventExistenceCheckByIdPort,
        ProductProcessedRatingEventCreationPort {

    private final ProductProcessedRatingEventMongoRepository repository;

    @Override
    public boolean exists(
            UUID eventId) {
        return this.repository.existsById(eventId);
    }

    @Override
    public void create(
            UUID eventId) {
        final var doc = new ProductProcessedRatingEventDocument(eventId);
        this.repository.save(doc);
    }
}
