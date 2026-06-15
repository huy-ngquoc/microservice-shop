package vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductRatingLookupByProductIdPort {
    ProductRating loadByProductIdOrZero(
            final ProductId productId);
}
