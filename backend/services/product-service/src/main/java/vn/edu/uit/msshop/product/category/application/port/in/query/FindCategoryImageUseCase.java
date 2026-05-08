package vn.edu.uit.msshop.product.category.application.port.in.query;

import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface FindCategoryImageUseCase {
    CategoryImageView findImageById(
            final CategoryId id);
}
