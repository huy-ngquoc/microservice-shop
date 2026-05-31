package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;

public interface UpdateAllProductRatingsPort {
    void updateAll(
            final Collection<ProductRating> ratings);
}
