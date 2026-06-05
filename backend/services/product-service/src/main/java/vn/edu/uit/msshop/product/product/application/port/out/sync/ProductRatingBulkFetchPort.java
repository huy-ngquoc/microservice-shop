package vn.edu.uit.msshop.product.product.application.port.out.sync;

import java.time.Instant;
import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.sync.ProductRatingSnapshot;

public interface ProductRatingBulkFetchPort {
    Collection<ProductRatingSnapshot> fetchAll(
            final Instant rangeStartTime,
            final Instant rangeEndTime);
}
