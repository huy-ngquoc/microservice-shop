package vn.uit.edu.msshop.rating.application.dto.command;

import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserAvatar;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Username;

public record PostRatingCommand(RatingId id, Content content, ProductId productId, RatingPoint ratingPoint, UserAvatar userAvatar,UserId userId, Username username) {

}
