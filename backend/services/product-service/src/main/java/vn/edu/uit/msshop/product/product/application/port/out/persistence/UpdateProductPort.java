package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.Product;

public interface UpdateProductPort {
  Product update(final Product product);
}
