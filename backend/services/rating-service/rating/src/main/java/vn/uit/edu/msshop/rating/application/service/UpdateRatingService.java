package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.command.UpdateRatingCommand;
import vn.uit.edu.msshop.rating.application.port.in.UpdateRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingPort;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingPort;
import vn.uit.edu.msshop.rating.domain.event.RatingUpdated;
import vn.uit.edu.msshop.rating.domain.model.Rating;

@Service
@RequiredArgsConstructor
public class UpdateRatingService implements UpdateRatingUseCase {
    private final SaveRatingPort savePort;
    private final PublishRatingEvent eventPublisher;
    private final LoadRatingPort loadPort;

    @Override
    public void update(UpdateRatingCommand command) {
        Rating rating = loadPort.loadById(command.ratingId());
        final var updateInfo = Rating.UpdateInfo.builder().id(command.ratingId()).content(command.content().apply(rating.getContent())).ratingPoint(command.ratingPoint().apply(rating.getRatingPoint())).build();
        final var next = rating.applyUpdateInfo(updateInfo);
        final var saved = savePort.save(next);
        eventPublisher.publish(new RatingUpdated(saved.getId()));

    }

}
