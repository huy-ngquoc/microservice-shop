package vn.uit.edu.msshop.rating.adapter.in.web.response;

import java.util.UUID;

public record RatingResponse(UUID id, String content, String mediaURL, String mediaType, String mediaPublicId, UUID productId, UUID userId, String userName, String userAvatar, float ratingPoint) {

}
