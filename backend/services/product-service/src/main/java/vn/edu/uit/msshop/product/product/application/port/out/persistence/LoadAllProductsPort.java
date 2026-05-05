package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadAllProductsPort {
  List<Product> loadAllByIds(final Collection<ProductId> ids);
}
