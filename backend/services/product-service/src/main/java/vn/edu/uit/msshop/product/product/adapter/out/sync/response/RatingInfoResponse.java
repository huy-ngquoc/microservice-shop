package vn.edu.uit.msshop.product.product.adapter.out.sync.response;

import java.time.Instant;
import java.util.UUID;

public record RatingInfoResponse(
        UUID productId,
        long ratingCount,
        long totalPoint,
        Instant createAt,
        Instant updateAt) {
}
