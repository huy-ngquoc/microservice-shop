package vn.edu.uit.msshop.product.variant.application.dto.command.count;

import java.time.Instant;

public record VariantStockCountBulkReconciliationCommand(
        Instant rangeStartTime,
        Instant rangeEndTime) {
}
