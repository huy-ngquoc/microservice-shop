package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantBulkDeletionByProductIdPort {
    void deleteByProductId(
            final VariantProductId productId);
}
