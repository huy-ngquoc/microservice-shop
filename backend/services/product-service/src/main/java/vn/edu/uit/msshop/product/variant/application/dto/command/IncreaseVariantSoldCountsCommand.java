package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public record IncreaseVariantSoldCountsCommand(
        UUID eventId,
        Collection<Item> items) {
    public record Item(
            VariantId variantId,
            int soldCountIncrement) {
    }

    public IncreaseVariantSoldCountsCommand {
        items = List.copyOf(items);
    }
}
