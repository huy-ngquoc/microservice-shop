package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.ReconcileProductRatingsCommand;

public interface ReconcileProductRatingsUseCase {
    void execute(
            final ReconcileProductRatingsCommand command);
}
