package vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductRatingInitializationByIdPort {
    ProductRating initializeById(
            final ProductId id);
}
