package vn.edu.uit.msshop.product.product.application.port.out;

import vn.edu.uit.msshop.product.product.domain.model.NewProductVariants;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;

public interface CreateVariantsForNewProductPort {
    ProductVariants create(
            final ProductId id,
            final NewProductVariants newVariants);
}
