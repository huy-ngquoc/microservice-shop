package vn.edu.uit.msshop.product.product.application.service.command;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.product.application.port.in.command.ApplyRatingDeletedUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CheckProcessedRatingEventExistsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.CreateProcessedRatingEventPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.UpdateProductRatingPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplyRatingDeletedService
        implements ApplyRatingDeletedUseCase {
    private final LoadProductRatingPort loadRatingPort;
    private final UpdateProductRatingPort updateRatingPort;
    private final CheckProcessedRatingEventExistsPort checkProcessedEventExistsPort;
    private final CreateProcessedRatingEventPort createProcessedEventPort;

    @Override
    @Transactional
    public void execute(
            final UUID eventId,
            final ProductId productId,
            final int point) {
        if (checkProcessedEventExistsPort.exists(eventId)) {
            log.debug("Skipping already-processed event {}", eventId);
            return;
        }

        try {
            final var rating = this.loadRatingPort.loadByIdOrZero(productId);

            final var next = rating.removeRating(point);
            this.updateRatingPort.update(next);
        } catch (final DomainException e) {
            log.error("Invariant violation for rating deleted event {} — marking processed without state change",
                    eventId, e);
        }

        this.createProcessedEventPort.create(eventId);
    }
}
