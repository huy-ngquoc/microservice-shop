package vn.edu.uit.msshop.product.category.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategoryImageActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;

public interface CategoryImageActiveLookupByIdUseCase {
    CategoryImageView find(
            final CategoryImageActiveLookupByIdQuery query);
}
