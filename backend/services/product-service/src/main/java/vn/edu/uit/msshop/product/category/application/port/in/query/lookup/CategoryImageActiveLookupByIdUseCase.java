package vn.edu.uit.msshop.product.category.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryImageActiveLookupByIdUseCase {
    CategoryImageView findActiveImageById(
            final CategoryId id);
}
