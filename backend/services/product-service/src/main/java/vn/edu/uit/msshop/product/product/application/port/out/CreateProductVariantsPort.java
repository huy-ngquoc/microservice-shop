package vn.edu.uit.msshop.product.product.application.port.out;

import java.util.List;

import vn.edu.uit.msshop.product.product.application.dto.command.CreateProductVariantCommand;
import vn.edu.uit.msshop.product.product.domain.model.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariants;

// TODO: should "command" in here?
public interface CreateProductVariantsPort {
    ProductVariants create(
            final ProductId id,
            final List<CreateProductVariantCommand> variants);
}
