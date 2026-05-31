package vn.uit.edu.msshop.rating.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.adapter.out.persistence.RatingInfoDocument;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingCount;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingTotal;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UpdateAt;

@Component
public class RatingInfoEntityMapper {
    public RatingInfo toDomain(
            final RatingInfoDocument doc) {
        final var productId = new ProductId(doc.getProductId());
        final var ratingCount = new RatingCount(doc.getRatingCount());
        final var ratingTotal = new RatingTotal(doc.getRatingTotal());
        final var createAt = new CreateAt(doc.getCreateAt());
        final var updateAt = new UpdateAt(doc.getUpdateAt());

        return new RatingInfo(
                productId,
                ratingCount,
                ratingTotal,
                createAt,
                updateAt);
    }

    public RatingInfoDocument toDocument(
            final RatingInfo ratingInfo) {
        return new RatingInfoDocument(
                ratingInfo.getProductId().value(),
                ratingInfo.getRatingCount().value(),
                ratingInfo.getRatingTotal().value(),
                ratingInfo.getCreateAt().value(),
                ratingInfo.getUpdateAt().value());
    }
}
