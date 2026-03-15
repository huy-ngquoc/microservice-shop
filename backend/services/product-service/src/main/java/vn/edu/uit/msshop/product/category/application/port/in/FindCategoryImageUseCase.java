package vn.edu.uit.msshop.product.category.application.port.in;

import vn.edu.uit.msshop.product.category.application.dto.query.CategoryImageView;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;

public interface FindCategoryImageUseCase {
    CategoryImageView findImageById(
            final CategoryId id);
}
