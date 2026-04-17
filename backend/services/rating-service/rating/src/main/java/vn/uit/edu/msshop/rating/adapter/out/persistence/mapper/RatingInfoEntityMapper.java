package vn.uit.edu.msshop.rating.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.adapter.out.persistence.RatingInfoDocument;
import vn.uit.edu.msshop.rating.domain.model.RatingInfo;
import vn.uit.edu.msshop.rating.domain.model.valueobject.CreateAt;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingCount;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UpdateAt;


@Component
public class RatingInfoEntityMapper {
    public RatingInfo toDomain(RatingInfoDocument document) {
        final var draft = RatingInfo.Draft.builder().productId(new ProductId(document.getProductId())).ratingCount(new RatingCount(document.getRatingCount())).totalPoint(new RatingPoint(document.getTotalPoint()))
        .createAt(new CreateAt(document.getCreateAt()))
        .updateAt(new UpdateAt(document.getUpdateAt()))
        .build();
        return RatingInfo.create(draft);
    }
    public RatingInfoDocument toDocument(RatingInfo ratingInfo) {
        return new RatingInfoDocument(ratingInfo.getProductId().value(), ratingInfo.getRatingCount().value(), ratingInfo.getTotalPoint().value(), ratingInfo.getCreateAt().value(), ratingInfo.getUpdateAt().value());
    }
}
