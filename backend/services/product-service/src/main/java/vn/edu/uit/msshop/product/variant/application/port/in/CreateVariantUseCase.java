package vn.edu.uit.msshop.product.variant.application.port.in;

import vn.edu.uit.msshop.product.variant.application.dto.command.CreateVariantCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;

public interface CreateVariantUseCase {
    VariantView create(
            final CreateVariantCommand command);
}
