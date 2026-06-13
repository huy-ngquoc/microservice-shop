package vn.edu.uit.msshop.product.variant.application.dto.command.sync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record VariantTraitBulkUpdateByIdsForProductCommand(
        Map<UUID, List<String>> traitListById) {

    public VariantTraitBulkUpdateByIdsForProductCommand {
        final var defensiveCopy = HashMap.<UUID, List<String>>newHashMap(traitListById.size());

        for (final var entry : traitListById.entrySet()) {
            final var id = entry.getKey();
            final var traitList = entry.getValue();
            final var immutableTraitList = List.copyOf(traitList);

            defensiveCopy.put(id, immutableTraitList);
        }

        traitListById = Map.copyOf(defensiveCopy);
    }
}
