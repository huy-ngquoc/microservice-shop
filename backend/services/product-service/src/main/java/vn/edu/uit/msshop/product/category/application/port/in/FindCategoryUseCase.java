package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public interface FindCategoryUseCase {
    CategoryView findById(
            final CategoryId id);
}
