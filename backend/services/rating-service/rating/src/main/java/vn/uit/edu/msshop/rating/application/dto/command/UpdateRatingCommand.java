package vn.uit.edu.msshop.rating.application.dto.command;
import vn.uit.edu.msshop.rating.application.common.Change;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;

public record UpdateRatingCommand(RatingId ratingId, Change<Content> content, Change<RatingPoint> ratingPoint ) {

}
