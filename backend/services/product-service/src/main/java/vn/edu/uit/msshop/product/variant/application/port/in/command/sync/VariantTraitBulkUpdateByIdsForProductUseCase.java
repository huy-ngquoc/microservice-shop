package vn.edu.uit.msshop.product.variant.application.port.in.command.sync;

import vn.edu.uit.msshop.product.variant.application.dto.command.sync.VariantTraitBulkUpdateByIdsForProductCommand;

public interface VariantTraitBulkUpdateByIdsForProductUseCase {
    void updateTraitsByIds(
            final VariantTraitBulkUpdateByIdsForProductCommand cmd);
}
