package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.rating.ReconcileProductRatingsCommand;

public interface ProductRatingBulkReconciliationUseCase {
    void execute(
            final ReconcileProductRatingsCommand command);
}
