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
    public Rating toDomain(
            final RatingMongoDBEntity entity) {
        final var id = new RatingId(entity.getId());
        final var content = new Content(entity.getContent());
        final var productId = new ProductId(entity.getProductId());
        final var ratingPoint = new RatingPoint(entity.getRatingPoint());
        final var userAvatar = new UserAvatar(entity.getUserAvatar());
        final var userId = new UserId(entity.getUserId());
        final var username = new Username(entity.getUserName());

        final Media media;
        final var mediaDoc = entity.getMedia();
        if (mediaDoc != null) {
            final var url = mediaDoc.getUrl();
            final var type = mediaDoc.getType();
            final var publicId = mediaDoc.getPublicId();
            final var size = mediaDoc.getSize();

            media = new Media(type, url, size, publicId);
        } else {
            media = null;
        }

        return new Rating(
                id,
                content,
                media,
                productId,
                ratingPoint,
                userAvatar,
                userId,
                username);
    }

    public RatingMongoDBEntity toEntity(
            final Rating rating) {
        final MediaDocument mediaDoc;
        if (rating.getMedia() != null) {
            mediaDoc = new MediaDocument(
                    rating.getMedia().type(),
                    rating.getMedia().url(),
                    rating.getMedia().size(),
                    rating.getMedia().publicId());
        } else {
            mediaDoc = null;
        }

        return new RatingMongoDBEntity(
                rating.getId().value(),
                rating.getProductId().value(),
                rating.getUserId().value(),
                rating.getContent().value(),
                mediaDoc,
                rating.getUsername().value(),
                rating.getRatingPoint().value(),
                rating.getUserAvatar().value());
    }
}
