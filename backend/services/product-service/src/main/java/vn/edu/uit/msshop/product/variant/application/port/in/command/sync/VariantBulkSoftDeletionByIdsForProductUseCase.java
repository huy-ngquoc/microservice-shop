package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantBulkSoftDeletionByIdsForProductCommand;

public interface VariantBulkSoftDeletionByIdsForProductUseCase {
    void deleteByIds(
            final VariantBulkSoftDeletionByIdsForProductCommand cmd);
}
