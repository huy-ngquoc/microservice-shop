package vn.uit.edu.msshop.cart.application.port.out;

import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;

public interface DeleteCartPort {
    public void deleteCartItem(DeleteCartItemCommand command);
    public void clearCart(ClearCartCommand command);
}
