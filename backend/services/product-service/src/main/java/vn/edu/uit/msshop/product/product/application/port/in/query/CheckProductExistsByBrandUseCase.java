package vn.edu.uit.msshop.product.product.application.port.in.query;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

public interface CheckProductExistsByBrandUseCase {
    boolean existsByBrandId(
            final ProductBrandId brandId);
}
