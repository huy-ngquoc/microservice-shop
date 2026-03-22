package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

public interface CheckProductExistsPort {
    boolean existsById(
            final VariantProductId productId);
}
