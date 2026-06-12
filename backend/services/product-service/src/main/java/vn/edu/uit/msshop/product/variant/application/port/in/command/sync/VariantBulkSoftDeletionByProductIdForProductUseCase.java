package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public interface VariantBulkSoftDeletionByProductIdForProductUseCase {
    void deleteByProductId(
            final VariantProductId productId);
}
