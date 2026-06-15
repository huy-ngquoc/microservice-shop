package vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductRatingDeletionByProductIdPort {
    void deleteByProductId(
            final ProductId productId);
}
