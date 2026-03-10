package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductBrandId;

public interface CheckProductBrandExistsPort {
    boolean existsById(
            final ProductBrandId id);
}
