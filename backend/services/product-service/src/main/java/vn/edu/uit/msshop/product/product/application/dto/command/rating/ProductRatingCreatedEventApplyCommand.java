package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.util.UUID;

public record ProductRatingCreatedEventApplyCommand(
        UUID eventId,
        UUID productId,
        int ratingPoint) {
}
