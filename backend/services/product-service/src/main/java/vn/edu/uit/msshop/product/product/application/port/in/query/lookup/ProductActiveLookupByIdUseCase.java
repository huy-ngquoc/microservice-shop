package vn.edu.uit.msshop.product.product.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;

public interface ProductActiveLookupByIdUseCase {
    ProductView find(
            final ProductActiveLookupByIdQuery query);
}
