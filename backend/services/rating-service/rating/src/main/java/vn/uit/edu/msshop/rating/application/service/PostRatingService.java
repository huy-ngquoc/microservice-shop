package vn.uit.edu.msshop.rating.application.service;

import java.time.Instant;
import java.util.Optional;

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
import vn.uit.edu.msshop.rating.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingCount;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UpdateAt;

@Service
@RequiredArgsConstructor
public class PostRatingService implements PostRatingUseCase {
    private final SaveRatingPort savePort;
    private final PublishRatingEvent publishEvent;
    private final LoadRatingInfoPort loadRatingInfoPort;
    private final SaveRatingInfoPort saveRatingInfoPort;
    private final CheckProductPort checkProductPort;
    @Override
    public void post(PostRatingCommand command) {
        boolean isProductExist = checkProductPort.isProductExist(command.productId());
        if(isProductExist==false) {
            throw new ProductNotFoundException(command.productId());
        }
        final var draft = Rating.Draft.builder().id(command.id()).content(command.content()).productId(command.productId())
        .ratingPoint(command.ratingPoint()).userId(command.userId()).username(command.username()).userAvatar(command.userAvatar()).build();
        final var rating = Rating.create(draft);
        final var saved = this.savePort.save(rating);
        Optional<RatingInfo> ratingInfoOptional = loadRatingInfoPort.loadById(new ProductId(command.productId().value()));
        if(ratingInfoOptional.isEmpty()) {
            final var ratingInfoDraft = RatingInfo.Draft.builder().productId(command.productId()).ratingCount(new RatingCount(1)).totalPoint(command.ratingPoint())
            .createAt(new CreateAt(Instant.now()))
            .updateAt(new UpdateAt(null))
            .build();
            saveRatingInfoPort.save(RatingInfo.create(ratingInfoDraft));
        }
        else {
            final var ratingInfo = ratingInfoOptional.get();
            final var toSave = ratingInfo.increaseRatingPoint(command.ratingPoint());
            saveRatingInfoPort.save(toSave);
        }
        this.publishEvent.publish(new RatingPosted(saved.getId()));
    }

}
