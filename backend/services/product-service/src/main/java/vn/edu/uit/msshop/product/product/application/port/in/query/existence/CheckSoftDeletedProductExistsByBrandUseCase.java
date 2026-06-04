package vn.edu.uit.msshop.product.product.application.port.in.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

public interface CheckSoftDeletedProductExistsByBrandUseCase {
    boolean existsSoftDeletedByBrandId(
            final ProductBrandId brandId);
}
