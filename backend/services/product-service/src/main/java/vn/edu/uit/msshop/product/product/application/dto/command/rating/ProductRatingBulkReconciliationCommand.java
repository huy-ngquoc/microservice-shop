package vn.edu.uit.msshop.product.product.application.dto.command.rating;

import java.time.Instant;

public record ProductRatingBulkReconciliationCommand(
        Instant rangeStartTime,
        Instant rangeEndTime) {
}
