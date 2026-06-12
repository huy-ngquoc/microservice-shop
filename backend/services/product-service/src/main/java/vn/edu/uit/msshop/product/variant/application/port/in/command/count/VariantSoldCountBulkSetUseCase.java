package vn.edu.uit.msshop.product.variant.application.port.in.command.count;

import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantSoldCountsCommand;

public interface VariantSoldCountBulkSetUseCase {
    void execute(
            final SetAllVariantSoldCountsCommand command);
}
