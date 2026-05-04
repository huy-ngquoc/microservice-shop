package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

public interface CheckSoftDeletedProductExistsByBrandPort {
    boolean existsSoftDeletedByBrandId(
            final ProductBrandId brandId);
}
