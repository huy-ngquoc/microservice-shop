package vn.edu.uit.msshop.product.variant.application.port.in.command;

import vn.edu.uit.msshop.product.variant.application.dto.command.UpdateVariantInfoCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface UpdateVariantInfoUseCase {
    VariantView updateInfo(
            final UpdateVariantInfoCommand command);
}
