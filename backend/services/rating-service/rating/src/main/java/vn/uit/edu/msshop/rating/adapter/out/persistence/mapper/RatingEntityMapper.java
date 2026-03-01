package vn.uit.edu.msshop.rating.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.adapter.out.persistence.MediaDocument;
import vn.uit.edu.msshop.rating.adapter.out.persistence.RatingMongoDBEntity;
import vn.uit.edu.msshop.rating.domain.model.Rating;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Media;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserAvatar;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Username;

@Component
public class RatingEntityMapper {
    public Rating toDomain(RatingMongoDBEntity entity) {
        final var snapshot = Rating.Snapshot.builder().id(new RatingId(entity.getId())).content(new Content(entity.getContent()))
        .media(new Media(entity.getMedia().getType(),entity.getMedia().getUrl(),entity.getMedia().getSize(),entity.getMedia().getPublicId()))
        .productId(new ProductId(entity.getProductId()))
        .ratingPoint(new RatingPoint(entity.getRatingPoint()))
        .userId(new UserId(entity.getUserId()))
        .username(new Username(entity.getUserName()))
        .userAvatar(new UserAvatar(entity.getUserAvatar()))
        .build();
        return Rating.reconstitue(snapshot);
    } 
    public RatingMongoDBEntity toEntity(Rating rating) {
        MediaDocument media = new MediaDocument(rating.getMedia().type(), rating.getMedia().url(),rating.getMedia().size(), rating.getMedia().publicId());
        return new RatingMongoDBEntity(rating.getId().value(),rating.getProductId().value(),rating.getUserId().value(),rating.getContent().value(),media,rating.getUsername().value(),rating.getRatingPoint().value(),rating.getUserAvatar().value());
    }
}
