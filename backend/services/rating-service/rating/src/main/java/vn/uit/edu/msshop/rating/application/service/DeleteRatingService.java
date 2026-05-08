package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.port.in.DeleteRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.DeleteRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.domain.event.RatingDeleted;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;

@Service
@RequiredArgsConstructor
public class DeleteRatingService implements DeleteRatingUseCase {
    private final DeleteRatingPort deletePort;
    private final PublishRatingEvent eventPublisher;

    @Override
    public void delete(RatingId id) {
        deletePort.deleteById(id);
        eventPublisher.publish(new RatingDeleted(id));
    }

}
