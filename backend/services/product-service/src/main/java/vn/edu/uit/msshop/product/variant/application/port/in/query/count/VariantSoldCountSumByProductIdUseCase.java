package vn.edu.uit.msshop.product.variant.application.port.in.query.count;

import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantSoldCountSumByProductIdQuery;

public interface VariantSoldCountSumByProductIdUseCase {
    int sum(
            final VariantSoldCountSumByProductIdQuery query);
}
