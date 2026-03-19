package vn.edu.uit.msshop.product.category.application.port.out;

import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public interface CheckCategoryExistsPort {
    boolean existsById(
            final CategoryId id);
}
