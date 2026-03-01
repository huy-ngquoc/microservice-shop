package vn.uit.edu.msshop.rating.application.dto.query;

import java.util.UUID;

public record RatingView(UUID ratingId, String content, String mediaURL, String mediaType, String mediaPublicId, UUID productId, UUID userId, String userName, String userAvatar) {

}
