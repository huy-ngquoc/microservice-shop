package vn.uit.edu.msshop.cart.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.port.in.DeleteCartItemUseCase;
import vn.uit.edu.msshop.cart.application.port.out.DeleteCartPort;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;

@Service
@RequiredArgsConstructor
public class DeleteCartItemService implements DeleteCartItemUseCase {
    private final DeleteCartPort deletePort;
    private final VariantToUserPort variantToUserPort;

    @Override
    public void deleteCartItem(DeleteCartItemCommand command) {
        variantToUserPort.removeMapping(command.variantId(), command.userId());
        deletePort.deleteCartItem(command);
    }

    @Override
    public void deleteManyItems(List<DeleteCartItemCommand> commands) {
        for(DeleteCartItemCommand command: commands ) {
            variantToUserPort.removeMapping(command.variantId(), command.userId());
        }
        deletePort.deleteManyCartItems(commands);
    }
}
