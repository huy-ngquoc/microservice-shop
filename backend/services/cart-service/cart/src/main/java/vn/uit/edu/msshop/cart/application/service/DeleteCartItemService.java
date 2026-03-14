package vn.uit.edu.msshop.cart.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.port.in.DeleteCartItemUseCase;
import vn.uit.edu.msshop.cart.application.port.out.DeleteCartPort;

@Service
@RequiredArgsConstructor
public class DeleteCartItemService implements DeleteCartItemUseCase {
    private final DeleteCartPort deletePort;

    @Override
    public void deleteCartItem(DeleteCartItemCommand command) {
        deletePort.deleteCartItem(command);
    }
}
