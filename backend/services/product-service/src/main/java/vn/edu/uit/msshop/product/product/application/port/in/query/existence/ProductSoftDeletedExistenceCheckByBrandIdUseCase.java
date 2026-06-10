package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByBrandIdQuery;

public interface ProductSoftDeletedExistenceCheckByBrandIdUseCase {
    boolean exists(
            final ProductSoftDeletedExistenceCheckByBrandIdQuery query);
}
