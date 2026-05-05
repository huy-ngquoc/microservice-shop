package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;

public interface UpdateProductRatingPort {
  ProductRating update(final ProductRating rating);
}
