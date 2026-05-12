package vn.uit.edu.msshop.cart.application.port.in;

import java.util.List;

import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;

public interface DeleteCartItemUseCase {
    public void deleteCartItem(
            DeleteCartItemCommand command);

    public void deleteManyItems(
            List<DeleteCartItemCommand> command);
}
