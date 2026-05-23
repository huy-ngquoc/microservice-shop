package vn.edu.uit.msshop.product.product.application.dto.command;

import java.time.Instant;

public record ReconcileProductRatingsCommand(
        Instant rangeStartTime,
        Instant rangeEndTime) {
}
