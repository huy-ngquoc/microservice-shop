package vn.edu.uit.msshop.product.product.application.port.in.query;

import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface FindProductUseCase {
    ProductView findById(
            final ProductId id);
}
