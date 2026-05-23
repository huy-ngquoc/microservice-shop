package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.SetAllVariantSoldCountsCommand;

public interface SetAllVariantSoldCountsUseCase {
    void execute(
            final SetAllVariantSoldCountsCommand command);
}
