package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkUpdateCommand;

public interface ProductRatingBulkUpdateUseCase {
    void execute(
            final ProductRatingBulkUpdateCommand command);
}
