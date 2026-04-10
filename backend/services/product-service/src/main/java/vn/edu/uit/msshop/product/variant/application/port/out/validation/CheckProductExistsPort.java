package vn.edu.uit.msshop.product.variant.application.port.out.validation;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface CheckProductExistsPort {
    boolean existsById(
            final VariantProductId productId);
}
