package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadProductRatingPort {
  ProductRating loadByIdOrZero(final ProductId id);
}
