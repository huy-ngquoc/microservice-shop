package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface DeleteProductSoldCountPort {
    void deleteById(
            final ProductId id);
}
