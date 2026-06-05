package vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup;

import java.util.Optional;

import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductSoftDeletedLookupByIdPort {
    Optional<Product> loadSoftDeletedById(
            final ProductId id);
}
