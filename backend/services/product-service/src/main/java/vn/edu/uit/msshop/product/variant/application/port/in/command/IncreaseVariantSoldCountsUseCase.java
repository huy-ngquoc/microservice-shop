package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.IncreaseVariantSoldCountsCommand;

public interface IncreaseVariantSoldCountsUseCase {
    void execute(
            final IncreaseVariantSoldCountsCommand command);
}
