package vn.uit.edu.msshop.rating.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Media;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserAvatar;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Username;
@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Rating {
    private RatingId id;
    private Content content;
    private Media media;
    private ProductId productId;
    private RatingPoint ratingPoint;
    private UserAvatar userAvatar;
    private UserId userId;
    private Username username;
}
