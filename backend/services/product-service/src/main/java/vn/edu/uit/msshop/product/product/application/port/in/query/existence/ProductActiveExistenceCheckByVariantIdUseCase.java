package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByVariantIdQuery;

public interface ProductActiveExistenceCheckByVariantIdUseCase {
    boolean exists(
            final ProductActiveExistenceCheckByVariantIdQuery query);
}
