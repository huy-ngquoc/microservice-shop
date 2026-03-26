package vn.edu.uit.msshop.product.variant.application.port.out;

import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

public interface RemoveVariantFromProductPort {
    void removeFromProduct(
            VariantId id,
            VariantProductId productId);
}
