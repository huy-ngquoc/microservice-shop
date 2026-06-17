package vn.edu.uit.msshop.product.variant.application.dto.command.sync;

import java.util.Set;
import java.util.UUID;

public record VariantBulkSoftDeletionByIdsForProductCommand(
        Set<UUID> variantIdSet,
        UUID productId) {

    public VariantBulkSoftDeletionByIdsForProductCommand {
        variantIdSet = Set.copyOf(variantIdSet);
    }
}
