package vn.edu.uit.msshop.product.category.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategoryActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;

public interface CategoryActiveLookupByIdUseCase {
    CategoryView find(
            final CategoryActiveLookupByIdQuery query);
}
