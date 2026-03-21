package vn.edu.uit.msshop.product.variant.application.port.in;

import java.util.List;

import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantsForNewProductCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;

public interface CreateVariantsForNewProductUseCase {
    List<VariantView> create(
            final CreateVariantsForNewProductCommand command);
}
