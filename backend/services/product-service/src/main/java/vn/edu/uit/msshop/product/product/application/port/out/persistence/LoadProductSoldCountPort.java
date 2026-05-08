package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadProductSoldCountPort {
    ProductSoldCount loadByIdOrZero(
            final ProductId id);
}
