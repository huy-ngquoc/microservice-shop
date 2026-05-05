package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProduct;

public interface CreateProductPort {
  Product create(final NewProduct newProduct);
}
