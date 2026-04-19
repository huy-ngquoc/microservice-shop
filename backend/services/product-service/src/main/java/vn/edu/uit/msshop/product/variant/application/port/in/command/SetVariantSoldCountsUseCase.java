package vn.edu.uit.msshop.product.variant.application.port.in.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.variant.application.dto.sync.VariantOrderSoldCount;

public interface SetVariantSoldCountsUseCase {
    void execute(
            final Collection<VariantOrderSoldCount> orderSoldCounts);
}
