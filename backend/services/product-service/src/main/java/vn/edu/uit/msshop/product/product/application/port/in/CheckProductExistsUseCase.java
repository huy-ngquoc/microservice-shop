package vn.edu.uit.msshop.product.product.application.port.in;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;

public interface CheckProductExistsUseCase {
    boolean existsById(
            final ProductId id);
}
