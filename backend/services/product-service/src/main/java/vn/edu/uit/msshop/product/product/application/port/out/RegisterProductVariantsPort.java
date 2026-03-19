package vn.edu.uit.msshop.product.product.application.port.out;

import java.util.List;

import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;

public interface RegisterProductVariantsPort {
    List<ProductVariant> registerAll(
            final ProductId id,
            final List<ProductVariant> variants);
}
