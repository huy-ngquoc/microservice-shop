package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.query.VariantView;

public interface UpdateVariantInfoUseCase {
    VariantView updateInfo(
            final UpdateVariantInfoCommand command);
}
