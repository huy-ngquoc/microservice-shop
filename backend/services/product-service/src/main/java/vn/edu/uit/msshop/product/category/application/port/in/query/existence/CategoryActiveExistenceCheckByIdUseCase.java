package vn.edu.uit.msshop.product.category.application.port.in.query.existence;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryActiveExistenceCheckByIdUseCase {
    boolean existsById(
            final CategoryId id);
}
