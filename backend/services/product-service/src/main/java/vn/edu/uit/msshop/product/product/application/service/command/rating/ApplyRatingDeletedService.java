package vn.edu.uit.msshop.product.product.application.service.command.rating;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ApplyRatingDeletedUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProcessedRatingEventExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProcessedRatingEventCreationPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyRatingDeletedService
        implements ApplyRatingDeletedUseCase {
    private final ProductRatingLookupByIdPort ratingLookupByIdPort;
    private final ProductRatingUpdatePort ratingUpdatePort;
    private final ProcessedRatingEventExistenceCheckByIdPort processedRatingEventExistenceCheckByIdPort;
    private final ProcessedRatingEventCreationPort processedRatingEventCreationPort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            key = "#productId.value()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true)
            })
    public void execute(
            final UUID eventId,
            final ProductId productId,
            final int point) {
        if (processedRatingEventExistenceCheckByIdPort.exists(eventId)) {
            log.debug("Skipping already-processed event {}", eventId);
            return;
        }

        try {
            final var rating = this.ratingLookupByIdPort.loadByIdOrZero(productId);

            final var next = rating.removeRating(point);
            this.ratingUpdatePort.update(next);
        } catch (final DomainException e) {
            log.error("Invariant violation for rating deleted event {} — marking processed without state change",
                    eventId, e);
        }

        this.processedRatingEventCreationPort.create(eventId);
    }
}
