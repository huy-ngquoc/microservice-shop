package vn.uit.edu.msshop.cart.application.port.in;

import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;

public interface ClearCartUseCase {
    public void clear(ClearCartCommand command);
}
