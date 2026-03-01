package vn.edu.uit.msshop.product.application.port.in;

import vn.edu.uit.msshop.product.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

public interface FindCategoryImageUseCase {
    CategoryImageView findById(
            final CategoryId id);
}
