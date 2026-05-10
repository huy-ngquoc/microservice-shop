package vn.uit.edu.msshop.cart.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartInfoCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;

public interface UpdateCartInfoUseCase {
    public CartView updateInfo(
            List<UpdateCartInfoCommand> command);
}
