package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Collection;
import java.util.Map;

import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadAllProductStockCountsPort {
  Map<ProductId, ProductStockCount> loadAllByIds(final Collection<ProductId> ids);
}
