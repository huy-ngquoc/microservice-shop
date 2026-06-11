package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingCreatedEventApplyCommand;

public interface ProductRatingCreatedEventApplyUseCase {
    void apply(
            final ProductRatingCreatedEventApplyCommand cmd);
}
