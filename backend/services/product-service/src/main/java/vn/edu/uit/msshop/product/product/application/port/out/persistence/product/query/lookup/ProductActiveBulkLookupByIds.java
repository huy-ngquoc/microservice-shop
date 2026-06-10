package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductActiveBulkLookupByIds {
    List<Product> loadAllByIds(
            final Collection<ProductId> ids);
}
