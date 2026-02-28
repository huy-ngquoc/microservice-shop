package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

public interface FindCategoryUseCase {
    CategoryView findById(
            final CategoryId id);
}
