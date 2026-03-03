package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public interface DeleteCategoryImageUseCase {
    void deleteById(
            final CategoryId id);
}
