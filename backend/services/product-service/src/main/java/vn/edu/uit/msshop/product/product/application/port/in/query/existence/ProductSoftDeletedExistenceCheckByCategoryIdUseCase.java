package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByCategoryIdQuery;

public interface ProductSoftDeletedExistenceCheckByCategoryIdUseCase {
    boolean exists(
            final ProductSoftDeletedExistenceCheckByCategoryIdQuery query);
}
