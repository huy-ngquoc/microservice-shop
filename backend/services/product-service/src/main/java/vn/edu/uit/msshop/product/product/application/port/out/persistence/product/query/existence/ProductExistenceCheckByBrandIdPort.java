package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

public interface ProductExistenceCheckByBrandIdPort {
    boolean existsByBrandId(
            final ProductBrandId brandId);
}
