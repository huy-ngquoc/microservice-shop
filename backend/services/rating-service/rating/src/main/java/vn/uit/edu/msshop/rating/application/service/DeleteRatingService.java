package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.port.in.DeleteRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.domain.event.RatingDeleted;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

@Service
@RequiredArgsConstructor
public class DeleteRatingService implements DeleteRatingUseCase {
    private final LoadRatingPort loadPort;
    private final DeleteRatingPort deletePort;
    private final PublishRatingEvent eventPublisher;

    @Override
    @Transactional
    public void delete(
            RatingId id) {
        final var rating = this.loadPort.loadById(id);

        deletePort.deleteById(id);

        final var event = new RatingDeleted(
                rating.getId(),
                rating.getProductId(),
                rating.getRatingPoint());
        eventPublisher.publish(event);
    }

}
