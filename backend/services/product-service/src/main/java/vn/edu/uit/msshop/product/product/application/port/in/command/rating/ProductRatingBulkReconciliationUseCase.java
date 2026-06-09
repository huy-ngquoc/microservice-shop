package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkReconciliationCommand;

public interface ProductRatingBulkReconciliationUseCase {
    void execute(
            final ProductRatingBulkReconciliationCommand command);
}
