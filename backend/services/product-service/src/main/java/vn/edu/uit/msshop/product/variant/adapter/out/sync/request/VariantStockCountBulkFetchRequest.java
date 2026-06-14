package vn.edu.uit.msshop.product.variant.adapter.out.sync.request;

import java.time.Instant;

public record VariantStockCountBulkFetchRequest(
        Instant startFirst,
        Instant endFirst,
        Instant startSecond,
        Instant endSecond) {
}
