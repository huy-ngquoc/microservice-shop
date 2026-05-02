package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.DecreaseProductSoldCountsForVariantsCommand;

public interface DecreaseProductSoldCountsForVariantsUseCase {
    void execute(
            final DecreaseProductSoldCountsForVariantsCommand command);
}
