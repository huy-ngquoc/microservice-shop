package vn.edu.uit.msshop.product.variant.application.port.in.query.count;

import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantStockCountValueSumByProductIdQuery;

public interface VariantStockCountValueSumByProductIdUseCase {
    int sum(
            final VariantStockCountValueSumByProductIdQuery query);
}
