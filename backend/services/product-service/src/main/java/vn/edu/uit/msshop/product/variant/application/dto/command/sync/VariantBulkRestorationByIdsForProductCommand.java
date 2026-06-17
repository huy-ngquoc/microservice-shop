package vn.edu.uit.msshop.product.variant.application.dto.command.sync;

import java.util.Set;
import java.util.UUID;

public record VariantBulkRestorationByIdsForProductCommand(
        Set<UUID> variantIdSet,
        UUID productId) {

    public VariantBulkRestorationByIdsForProductCommand {
        variantIdSet = Set.copyOf(variantIdSet);
    }
}
