package vn.edu.uit.msshop.product.product.application.port.out.sync;

import java.time.Instant;
import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.sync.ProductRatingSnapshot;

public interface FetchProductRatingsPort {
    Collection<ProductRatingSnapshot> fetchAll(
            final Instant start,
            final Instant end);
}
