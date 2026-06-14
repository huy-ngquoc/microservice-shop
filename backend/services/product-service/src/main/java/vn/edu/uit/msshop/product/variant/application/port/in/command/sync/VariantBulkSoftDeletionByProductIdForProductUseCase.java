package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByProductIdForProductCommand;

public interface VariantBulkSoftDeletionByProductIdForProductUseCase {
    void softDeleteAll(
            final VariantBulkSoftDeletionByProductIdForProductCommand cmd);
}
