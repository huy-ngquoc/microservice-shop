package vn.edu.uit.msshop.product.product.application.dto.command.variant;

import java.util.List;
import java.util.UUID;

public record ProductVariantBulkRemovalCommand(
        UUID productId,
        List<UUID> variantIdList,
        long productVersion) {
    public ProductVariantBulkRemovalCommand {
        variantIdList = List.copyOf(variantIdList);
    }
}
