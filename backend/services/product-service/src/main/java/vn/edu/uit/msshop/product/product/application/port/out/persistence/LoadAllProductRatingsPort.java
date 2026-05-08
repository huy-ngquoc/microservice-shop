package vn.edu.uit.msshop.product.product.application.port.out.persistence;

import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface LoadAllProductRatingsPort {
    Map<ProductId, ProductRating> loadAllByIds(
            final Set<ProductId> ids);
}
