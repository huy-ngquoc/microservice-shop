package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.util.UUID;

public record ProductRatingUpdatedEventApplyCommand(
        UUID eventId,
        UUID productId,
        int oldRatingPoint,
        int newRatingPoint) {
}
