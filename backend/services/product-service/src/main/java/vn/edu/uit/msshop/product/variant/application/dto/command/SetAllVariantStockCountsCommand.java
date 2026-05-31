package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;

public record SetAllVariantStockCountsCommand(
        Collection<VariantInventoryStockCount> stockCounts) {
    public SetAllVariantStockCountsCommand {
        stockCounts = List.copyOf(stockCounts);
    }
}
