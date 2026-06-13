package vn.edu.uit.msshop.product.variant.application.port.in.command.lifecycle;

import vn.edu.uit.msshop.product.variant.application.dto.command.lifecycle.VariantInfoUpdateByIdCommand;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;

public interface VariantInfoUpdateByIdUseCase {
    VariantView updateInfo(
            final VariantInfoUpdateByIdCommand cmd);
}
