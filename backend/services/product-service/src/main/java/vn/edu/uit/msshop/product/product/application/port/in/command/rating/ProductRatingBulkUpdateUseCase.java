package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.SetAllProductRatingsCommand;

public interface ProductRatingBulkUpdateUseCase {
    void execute(
            final SetAllProductRatingsCommand command);
}
