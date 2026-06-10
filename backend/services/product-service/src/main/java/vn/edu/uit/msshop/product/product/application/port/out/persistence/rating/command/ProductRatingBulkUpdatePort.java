package vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;

public interface ProductRatingBulkUpdatePort {
    void updateAll(
            final Collection<ProductRating> ratings);
}
