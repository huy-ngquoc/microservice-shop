package vn.uit.edu.msshop.cart.application.port.in;

import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;

public interface DeleteCartItemUseCase {
    public void deleteCartItem(DeleteCartItemCommand command);
}
