package vn.edu.uit.msshop.product.product.application.port.in.command.rating;

import java.util.UUID;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ApplyRatingDeletedUseCase {
    void execute(
            final UUID eventId,
            final ProductId productId,
            final int point);
}
