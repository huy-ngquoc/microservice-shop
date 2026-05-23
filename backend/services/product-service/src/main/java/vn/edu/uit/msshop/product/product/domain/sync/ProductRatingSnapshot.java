package vn.edu.uit.msshop.product.product.domain.sync;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingTotal;

public record ProductRatingSnapshot(
        ProductId productId,
        ProductRatingTotal total,
        ProductRatingAmount amount) {
}
