package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadProductRatingPort {
    Optional<ProductRating> loadById(
            final ProductId id);

    default ProductRating loadByIdOrZero(
            final ProductId id) {
        return this.loadById(id)
                .orElse(ProductRating.zero(id));
    }
}
