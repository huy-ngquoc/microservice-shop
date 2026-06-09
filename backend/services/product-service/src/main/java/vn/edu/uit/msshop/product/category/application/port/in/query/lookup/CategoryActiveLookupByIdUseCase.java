package vn.edu.uit.msshop.product.category.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

public interface CategoryActiveLookupByIdUseCase {
    CategoryView findActiveById(
            final CategoryId id);
}
