package vn.edu.uit.msshop.product.variant.application.dto.command.sync;

import java.util.Set;
import java.util.UUID;

public record VariantBulkRestorationByIdsForProductCommand(
        Set<UUID> idSet) {

    public VariantBulkRestorationByIdsForProductCommand {
        idSet = Set.copyOf(idSet);
    }
}
