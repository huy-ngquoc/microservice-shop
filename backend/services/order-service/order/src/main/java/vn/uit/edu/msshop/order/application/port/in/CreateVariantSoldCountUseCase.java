package vn.uit.edu.msshop.order.application.port.in;

import vn.uit.edu.msshop.order.application.dto.command.CreateVariantSoldCountCommand;
import vn.uit.edu.msshop.order.application.dto.query.VariantSoldCountView;

public interface CreateVariantSoldCountUseCase {
    public VariantSoldCountView create(CreateVariantSoldCountCommand command);
}
