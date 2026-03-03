package vn.uit.edu.msshop.rating.adapter.in.web.mapper;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.rating.adapter.in.web.request.PostRatingRequest;
import vn.uit.edu.msshop.rating.adapter.in.web.request.UpdateRatingRequest;
import vn.uit.edu.msshop.rating.adapter.in.web.request.common.ChangeRequest;
import vn.uit.edu.msshop.rating.adapter.in.web.response.RatingResponse;
import vn.uit.edu.msshop.rating.application.dto.command.PostRatingCommand;
import vn.uit.edu.msshop.rating.application.dto.command.UpdateRatingCommand;
import vn.uit.edu.msshop.rating.application.dto.query.RatingView;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Content;
import vn.uit.edu.msshop.rating.domain.model.valueobject.ProductId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.RatingPoint;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserAvatar;
import vn.uit.edu.msshop.rating.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.rating.domain.model.valueobject.Username;
/*UUID productId,
    UUID userId,
    String userName,
    String userAvatar,
    float ratingPoint*/

@Component
public class RatingWebMapper {
    public PostRatingCommand toCommand(PostRatingRequest request) {
        final var id = new RatingId(request.ratingId());
        final var content =new Content(request.content());
        final var productId = new ProductId(request.productId());
        final var userId = new UserId(request.userId());
        final var userName = new Username(request.userName());
        final var userAvatar = new UserAvatar(request.userAvatar());
        final var ratingPoint = new RatingPoint(request.ratingPoint());
        return new PostRatingCommand(id, content, productId, ratingPoint, userAvatar, userId, userName);
    }
    public UpdateRatingCommand toCommand(UpdateRatingRequest request) {
        final var id = new RatingId(request.id());
        final var content = ChangeRequest.toChange(request.content(), Content::new);
        final var ratingPoint = ChangeRequest.toChange(request.ratingPoint(), RatingPoint::new);
        return new UpdateRatingCommand(id,content,ratingPoint);
    }
    public RatingResponse toResponse(RatingView view) {
        return new RatingResponse(view.ratingId(), view.content(), view.mediaURL(), view.mediaType(),view.mediaPublicId(), view.productId(), view.userId(), view.userName(), view.userAvatar());
    }
}
