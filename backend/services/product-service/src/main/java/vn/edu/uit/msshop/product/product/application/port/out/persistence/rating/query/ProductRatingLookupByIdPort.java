package vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductRatingLookupByIdPort {
    ProductRating loadByIdOrZero(
            final ProductId id);
}
