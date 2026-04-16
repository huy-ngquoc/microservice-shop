package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.command.UpdateRatingCommand;
import vn.uit.edu.msshop.rating.application.exception.RatingInfoNotFoundException;
import vn.uit.edu.msshop.rating.application.port.in.UpdateRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingPort;
import vn.uit.edu.msshop.rating.domain.event.RatingUpdated;
import vn.uit.edu.msshop.rating.domain.model.Rating;

@Service
@RequiredArgsConstructor
public class UpdateRatingService implements UpdateRatingUseCase {
    private final SaveRatingPort savePort;
    private final PublishRatingEvent eventPublisher;
    private final LoadRatingPort loadPort;
    private final LoadRatingInfoPort loadRatingInfoPort;
    private final SaveRatingInfoPort saveRatingInfoPort;

    @Override
    @Transactional
    public void update(UpdateRatingCommand command) {
        Rating rating = loadPort.loadById(command.ratingId());
        
        final var updateInfo = Rating.UpdateInfo.builder().id(command.ratingId()).content(command.content().apply(rating.getContent())).ratingPoint(command.ratingPoint().apply(rating.getRatingPoint())).build();
        if(rating.isRatingPointChange(updateInfo)) {
            var ratingInfo = loadRatingInfoPort.loadById(rating.getProductId()).orElseThrow(()->new RatingInfoNotFoundException(rating.getProductId()));
            ratingInfo=ratingInfo.decreaseRatingPoint(rating.getRatingPoint());
            ratingInfo = ratingInfo.increaseRatingPoint(updateInfo.ratingPoint());
            saveRatingInfoPort.save(ratingInfo);
        }
        final var next = rating.applyUpdateInfo(updateInfo);
        final var saved = savePort.save(next);
        
        eventPublisher.publish(new RatingUpdated(saved.getId()));

    }

}
