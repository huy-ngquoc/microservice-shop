package vn.edu.uit.msshop.product.category.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategorySoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;

public interface CategorySoftDeletedLookupByIdUseCase {
    CategoryView find(
            final CategorySoftDeletedLookupByIdQuery query);
}
