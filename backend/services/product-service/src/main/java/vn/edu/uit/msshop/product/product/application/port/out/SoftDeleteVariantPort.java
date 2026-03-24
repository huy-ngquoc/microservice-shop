package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariantVersion;

public interface SoftDeleteVariantPort {
    void deleteById(
            final ProductVariantId variantId,
            final ProductVariantVersion expectedVersion);
}
