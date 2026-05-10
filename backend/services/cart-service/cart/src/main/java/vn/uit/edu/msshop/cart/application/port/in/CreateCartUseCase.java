package vn.uit.edu.msshop.cart.application.port.in;

import vn.uit.edu.msshop.cart.application.dto.command.CreateCartCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;

public interface CreateCartUseCase {
    public CartView create(
            CreateCartCommand command);
}
