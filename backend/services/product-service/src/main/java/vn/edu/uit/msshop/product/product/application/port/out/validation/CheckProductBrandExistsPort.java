package vn.edu.uit.msshop.product.product.application.port.out.validation;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

public interface CheckProductBrandExistsPort {
    boolean existsById(
            final ProductBrandId brandId);
}
