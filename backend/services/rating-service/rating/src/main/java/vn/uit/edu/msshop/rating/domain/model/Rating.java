package vn.uit.edu.msshop.rating.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.shared.domain.Domains;
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
public final class Rating {
    @EqualsAndHashCode.Include
    private final RatingId id;

    private final Content content;

    private final Media media;

    private final ProductId productId;

    private final RatingPoint ratingPoint;

    private final UserAvatar userAvatar;

    private final UserId userId;

    private final Username username;

    public Rating(
            final RatingId id,
            final Content content,
            final Media media,
            final ProductId productId,
            final RatingPoint ratingPoint,
            final UserAvatar userAvatar,
            final UserId userId,
            final Username username) {
        this.id = Domains.requireNonNull(id, "Rating ID must not be null");
        this.content = content;
        this.media = media;
        this.productId = Domains.requireNonNull(productId, "Product ID must not be null");
        this.ratingPoint = Domains.requireNonNull(ratingPoint, "Rating point must not be null");
        this.userAvatar = userAvatar;
        this.userId = Domains.requireNonNull(userId, "User ID must not be null");
        this.username = Domains.requireNonNull(username, "Username must not be null");
    }
}
