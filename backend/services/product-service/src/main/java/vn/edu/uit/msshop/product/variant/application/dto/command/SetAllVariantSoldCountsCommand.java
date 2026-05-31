package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantOrderSoldCount;

public record SetAllVariantSoldCountsCommand(
        Collection<VariantOrderSoldCount> orderSoldCounts) {
    public SetAllVariantSoldCountsCommand {
        orderSoldCounts = List.copyOf(orderSoldCounts);
    }
}
