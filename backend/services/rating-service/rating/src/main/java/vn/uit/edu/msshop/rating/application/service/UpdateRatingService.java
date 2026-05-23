package vn.uit.edu.msshop.rating.application.service;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.shared.application.dto.Change;
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
import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;

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
    public void update(
            UpdateRatingCommand command) {
        final var contentSet = command.content().getSet();
        final var ratingPointSet = command.ratingPoint().getSet();

        if ((contentSet == null) && (ratingPointSet == null)) {
            return;
        }

        final var rating = loadPort.loadById(command.ratingId());
        final var next = this.applyChanges(
                rating,
                contentSet,
                ratingPointSet);
        if (next == null) {
            return;
        }

        final var saved = savePort.save(next);

        final var event = new RatingUpdated(
                saved.getId(),
                saved.getProductId(),
                rating.getRatingPoint(),
                saved.getRatingPoint());
        eventPublisher.publish(event);
    }

    private @Nullable Rating applyChanges(
            final Rating current,
            final Change.@Nullable Set<Content> contentSet,
            final Change.@Nullable Set<RatingPoint> ratingPointSet) {
        final var applyContentResult = Change.Set.applyChange(
                contentSet,
                current.getContent());
        final var applyRatingPointResult = Change.Set.applyChange(
                ratingPointSet,
                current.getRatingPoint());

        if (!applyContentResult.changed() && !applyRatingPointResult.changed()) {
            return null;
        }

        if (applyRatingPointResult.changed()) {
            final var ratingInfo = loadRatingInfoPort.loadById(current.getProductId())
                    .orElseThrow(() -> new RatingInfoNotFoundException(current.getProductId()));

            final var next = ratingInfo.changeRating(
                    current.getRatingPoint(),
                    applyRatingPointResult.newValue());
            this.saveRatingInfoPort.save(next);
        }

        return new Rating(
                current.getId(),
                applyContentResult.newValue(),
                current.getMedia(),
                current.getProductId(),
                applyRatingPointResult.newValue(),
                current.getUserAvatar(),
                current.getUserId(),
                current.getUsername());
    }
}
