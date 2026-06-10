package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByBrandIdQuery;

public interface ProductActiveExistenceCheckByBrandIdUseCase {
    boolean exists(
            final ProductActiveExistenceCheckByBrandIdQuery query);
}
