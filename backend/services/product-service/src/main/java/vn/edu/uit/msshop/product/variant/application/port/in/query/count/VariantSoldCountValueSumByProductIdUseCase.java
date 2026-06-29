package vn.edu.uit.msshop.product.variant.application.port.in.query.count;

import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantSoldCountValueSumByProductIdQuery;

public interface VariantSoldCountValueSumByProductIdUseCase {
    int sum(
            final VariantSoldCountValueSumByProductIdQuery query);
}
