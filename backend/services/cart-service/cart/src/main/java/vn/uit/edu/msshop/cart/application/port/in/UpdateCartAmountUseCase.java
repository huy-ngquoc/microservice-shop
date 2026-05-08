package vn.uit.edu.msshop.cart.application.port.in;

import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartAmountCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;

public interface UpdateCartAmountUseCase {
    public CartView update(UpdateCartAmountCommand command);
}
