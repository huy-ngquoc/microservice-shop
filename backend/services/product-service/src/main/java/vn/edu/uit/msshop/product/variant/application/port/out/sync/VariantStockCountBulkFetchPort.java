package vn.edu.uit.msshop.product.variant.application.port.out.sync;

import java.time.Instant;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantStockCountSnapshot;

public interface VariantStockCountBulkFetchPort {
    List<VariantStockCountSnapshot> fetchAll(
            final Instant rangeStartTime,
            final Instant rangeEndTime);
}
