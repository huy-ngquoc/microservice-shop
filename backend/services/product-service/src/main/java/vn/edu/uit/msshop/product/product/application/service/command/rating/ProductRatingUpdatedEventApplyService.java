package vn.edu.uit.msshop.product.product.application.service.command.rating;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingUpdatedEventApplyCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingUpdatedEventApplyUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingUpdatePort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProductProcessedRatingEventExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ratingevent.ProductProcessedRatingEventCreationPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductRatingUpdatedEventApplyService
        implements ProductRatingUpdatedEventApplyUseCase {

    private final ProductRatingLookupByIdPort ratingLookupByIdPort;
    private final ProductRatingUpdatePort ratingUpdatePort;
    private final ProductProcessedRatingEventExistenceCheckByIdPort ratingEventExistenceCheckByIdPort;
    private final ProductProcessedRatingEventCreationPort processedRatingEventCreationPort;

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
    public void apply(
            final ProductRatingUpdatedEventApplyCommand cmd) {
        final var eventId = cmd.eventId();
        if (ratingEventExistenceCheckByIdPort.exists(eventId)) {
            log.debug("Skipping already-processed event {}", eventId);
            return;
        }

        try {
            final var productId = new ProductId(cmd.productId());
            final var rating = this.ratingLookupByIdPort.loadByIdOrZero(productId);

            final var next = rating.updateRating(
                    cmd.oldRatingPoint(),
                    cmd.newRatingPoint());
            this.ratingUpdatePort.update(next);
        } catch (final DomainException e) {
            log.error("Invariant violation for rating update event {} — marking processed without state change",
                    eventId, e);
        }

        this.processedRatingEventCreationPort.create(eventId);
    }
}
