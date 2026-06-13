package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkHardDeletionByProductIdForProductCommand;

public interface VariantBulkHardDeletionByProductIdForProductUseCase {
    void purgeByProductId(
            final VariantBulkHardDeletionByProductIdForProductCommand cmd);
}
