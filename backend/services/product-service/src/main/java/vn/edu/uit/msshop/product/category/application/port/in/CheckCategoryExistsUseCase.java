package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public interface CheckCategoryExistsUseCase {
    boolean existsById(
            final CategoryId id);
}
