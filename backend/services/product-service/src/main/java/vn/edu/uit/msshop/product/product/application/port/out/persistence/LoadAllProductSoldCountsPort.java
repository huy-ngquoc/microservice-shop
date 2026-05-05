package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadAllProductSoldCountsPort {
  Map<ProductId, ProductSoldCount> loadAllByIds(final Set<ProductId> ids);
}
