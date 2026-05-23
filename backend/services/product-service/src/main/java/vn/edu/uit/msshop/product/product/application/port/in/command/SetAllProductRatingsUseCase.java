package vn.edu.uit.msshop.product.product.application.port.in.command;

import vn.edu.uit.msshop.product.product.application.dto.command.SetAllProductRatingsCommand;

public interface SetAllProductRatingsUseCase {
    void execute(
            final SetAllProductRatingsCommand command);
}
