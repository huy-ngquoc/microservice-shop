package vn.edu.uit.msshop.product.product.application.port.in.command;

import java.util.UUID;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ApplyRatingUpdatedUseCase {
    void execute(
            final UUID eventId,
            final ProductId productId,
            final int oldPoint,
            final int newPoint);
}
