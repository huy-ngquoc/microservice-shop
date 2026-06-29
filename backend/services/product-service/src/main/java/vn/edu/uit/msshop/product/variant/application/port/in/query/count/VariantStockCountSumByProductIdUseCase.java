package vn.edu.uit.msshop.product.variant.application.port.in.query.count;

import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantStockCountSumByProductIdQuery;

public interface VariantStockCountSumByProductIdUseCase {
    int sum(
            final VariantStockCountSumByProductIdQuery query);
}
