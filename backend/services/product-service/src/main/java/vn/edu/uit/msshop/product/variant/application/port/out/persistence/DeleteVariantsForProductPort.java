package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface DeleteVariantsForProductPort {
    void deleteByProductId(
            final VariantProductId productId);
}
