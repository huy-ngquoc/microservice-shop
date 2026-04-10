package vn.edu.uit.msshop.product.category.application.port.in.query;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface FindSoftDeletedCategoryUseCase {
    CategoryView findSoftDeletedById(
            final CategoryId id);
}
