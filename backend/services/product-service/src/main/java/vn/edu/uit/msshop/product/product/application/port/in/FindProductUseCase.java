package vn.edu.uit.msshop.product.product.application.port.in;

import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;

public interface FindProductUseCase {
    ProductView findById(
            final ProductId id);
}
