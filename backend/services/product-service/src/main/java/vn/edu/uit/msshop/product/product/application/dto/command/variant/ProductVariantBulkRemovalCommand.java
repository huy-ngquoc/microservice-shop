package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.Set;
import java.util.UUID;

public record ProductVariantBulkRemovalCommand(
        UUID productId,
        Set<UUID> variantIdSet,
        long productVersion) {
    public ProductVariantBulkRemovalCommand {
        variantIdSet = Set.copyOf(variantIdSet);
    }
}
