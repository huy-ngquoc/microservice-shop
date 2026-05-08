package vn.edu.uit.msshop.product.category.application.port.in.query;

import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CheckCategoryExistsUseCase {
    boolean existsById(
            final CategoryId id);
}
