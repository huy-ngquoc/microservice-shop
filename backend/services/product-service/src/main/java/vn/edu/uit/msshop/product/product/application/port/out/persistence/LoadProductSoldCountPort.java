package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Optional;

import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadProductSoldCountPort {
    Optional<ProductSoldCount> loadById(
            final ProductId id);

    default ProductSoldCount loadByIdOrZero(
            final ProductId id) {
        return this.loadById(id)
                .orElse(ProductSoldCount.zero(id));
    }
}
