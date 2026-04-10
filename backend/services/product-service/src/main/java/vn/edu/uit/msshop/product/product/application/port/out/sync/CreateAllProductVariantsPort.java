package vn.edu.uit.msshop.product.product.application.port.out.sync;

import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.creation.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

public interface CreateAllProductVariantsPort {
    ProductVariants create(
            final ProductId id,
            final NewProductVariants newVariants);
}
