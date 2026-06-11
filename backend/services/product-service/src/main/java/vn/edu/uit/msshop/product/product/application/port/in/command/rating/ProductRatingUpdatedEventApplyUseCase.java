package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingUpdatedEventApplyCommand;

public interface ProductRatingUpdatedEventApplyUseCase {
    void apply(
            final ProductRatingUpdatedEventApplyCommand cmd);
}
