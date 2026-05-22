package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.command.PostRatingCommand;
import vn.uit.edu.msshop.rating.application.exception.ProductNotFoundException;
import vn.uit.edu.msshop.rating.application.port.in.PostRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.CheckProductPort;
import vn.uit.edu.msshop.rating.application.port.out.LoadRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingInfoPort;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingPort;
import vn.uit.edu.msshop.rating.domain.event.RatingPosted;
import vn.uit.edu.msshop.rating.domain.model.Rating;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;

@Service
@RequiredArgsConstructor
public class PostRatingService implements PostRatingUseCase {
    private final SaveRatingPort savePort;
    private final PublishRatingEvent publishEvent;
    private final LoadRatingInfoPort loadRatingInfoPort;
    private final SaveRatingInfoPort saveRatingInfoPort;
    private final CheckProductPort checkProductPort;

    @Override
    public void post(
            PostRatingCommand command) {
        final var productExisted = checkProductPort.isProductExist(command.productId());
        if (!productExisted) {
            throw new ProductNotFoundException(command.productId());
        }

        final var rating = new Rating(
                command.id(),
                command.content(),
                null,
                command.productId(),
                command.ratingPoint(),
                command.userAvatar(),
                command.userId(),
                command.username());
        final var saved = this.savePort.save(rating);

        this.updateRatingInfo(
                command.productId(),
                command.ratingPoint());

        this.publishEvent.publish(new RatingPosted(saved.getId()));
    }

    private void updateRatingInfo(
            final ProductId productId,
            final RatingPoint ratingPoint) {
        final var ratingInfoOptional = loadRatingInfoPort
                .loadById(productId);

        final RatingInfo toSave;
        if (ratingInfoOptional.isEmpty()) {
            toSave = RatingInfo.newRating(
                    productId,
                    ratingPoint);
        } else {
            final var ratingInfo = ratingInfoOptional.get();
            toSave = ratingInfo.addRating(ratingPoint);
        }

        this.saveRatingInfoPort.save(toSave);
    }
}
