package vn.uit.edu.msshop.rating.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.rating.application.dto.command.PostRatingCommand;
import vn.uit.edu.msshop.rating.application.port.in.PostRatingUseCase;
import vn.uit.edu.msshop.rating.application.port.out.PublishRatingEvent;
import vn.uit.edu.msshop.rating.application.port.out.SaveRatingPort;
import vn.uit.edu.msshop.rating.domain.event.RatingPosted;
import vn.uit.edu.msshop.rating.domain.model.Rating;
@Service
@RequiredArgsConstructor
public class PostRatingService implements PostRatingUseCase {
    private final SaveRatingPort savePort;
    private final PublishRatingEvent publishEvent;
    @Override
    public void post(PostRatingCommand command) {
        final var draft = Rating.Draft.builder().id(command.id()).content(command.content()).media(command.media()).productId(command.productId())
        .ratingPoint(command.ratingPoint()).userId(command.userId()).username(command.username()).userAvatar(command.userAvatar()).build();
        final var rating = Rating.create(draft);
        final var saved = this.savePort.save(rating);
        this.publishEvent.publish(new RatingPosted(saved.getId()));
    }

}
