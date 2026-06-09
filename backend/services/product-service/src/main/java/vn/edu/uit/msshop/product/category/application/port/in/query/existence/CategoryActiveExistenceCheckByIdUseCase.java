package vn.edu.uit.msshop.product.category.application.port.in.query.existence;

import vn.edu.uit.msshop.product.category.application.dto.query.existence.CategoryActiveExistenceCheckByIdQuery;

public interface CategoryActiveExistenceCheckByIdUseCase {
    boolean exists(
            final CategoryActiveExistenceCheckByIdQuery query);
}
