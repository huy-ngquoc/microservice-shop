package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;

public interface UpdateVariantInfoPort {
    ProductVariant updateInfo(
            final ProductVariant variant);
}
