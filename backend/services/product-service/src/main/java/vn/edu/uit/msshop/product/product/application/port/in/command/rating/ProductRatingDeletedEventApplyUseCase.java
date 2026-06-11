package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingDeletedEventApplyCommand;

public interface ProductRatingDeletedEventApplyUseCase {
    void apply(
            final ProductRatingDeletedEventApplyCommand cmd);
}
