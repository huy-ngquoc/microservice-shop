package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadProductPort {
  Optional<Product> loadById(final ProductId id);
}
