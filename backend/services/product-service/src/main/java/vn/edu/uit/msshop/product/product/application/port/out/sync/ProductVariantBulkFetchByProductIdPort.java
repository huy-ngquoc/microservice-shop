package vn.edu.uit.msshop.product.product.application.port.out.sync;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface ProductVariantBulkFetchByProductIdPort {
    List<ProductVariant> fetchAllActiveByProductId(
            final ProductId productId);
}
