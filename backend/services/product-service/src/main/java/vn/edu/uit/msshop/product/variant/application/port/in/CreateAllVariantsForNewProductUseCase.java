package vn.edu.uit.msshop.product.variant.application.port.in;

import java.util.List;

import vn.edu.uit.msshop.product.variant.application.dto.command.CreateAllVariantsCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;

public interface CreateAllVariantsForNewProductUseCase {
    List<VariantView> createAllForNewProduct(
            final CreateAllVariantsCommand command);
}
