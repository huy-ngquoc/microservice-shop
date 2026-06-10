package vn.edu.uit.msshop.product.product.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductSoftDeletedLookupByIdUseCase {
    ProductView find(
            final ProductSoftDeletedLookupByIdQuery query);
}
