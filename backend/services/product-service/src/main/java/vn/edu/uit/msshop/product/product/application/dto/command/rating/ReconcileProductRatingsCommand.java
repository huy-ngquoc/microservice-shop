package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.time.Instant;

public record ReconcileProductRatingsCommand(
        Instant rangeStartTime,
        Instant rangeEndTime) {
}
