package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

public interface DeleteCategoryImageUseCase {
    void deleteById(
            final CategoryId id);
}
