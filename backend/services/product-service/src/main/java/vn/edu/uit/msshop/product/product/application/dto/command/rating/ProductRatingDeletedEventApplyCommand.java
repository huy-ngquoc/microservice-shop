package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.util.UUID;

public record ProductRatingDeletedEventApplyCommand(
        UUID eventId,
        UUID productId,
        int ratingPoint) {
}
