package vn.edu.uit.msshop.product.product.application.port.in.query.lookup;

import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductActiveLookupByIdUseCase {
    ProductView findById(
            final ProductId id);
}
