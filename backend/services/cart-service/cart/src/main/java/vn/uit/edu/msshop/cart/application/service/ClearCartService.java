package vn.uit.edu.msshop.cart.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;
import vn.uit.edu.msshop.cart.application.port.in.ClearCartUseCase;
import vn.uit.edu.msshop.cart.application.port.out.DeleteCartPort;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;

@Service
@RequiredArgsConstructor
public class ClearCartService implements ClearCartUseCase {
    private final DeleteCartPort deletePort;
    private final VariantToUserPort variantToUserPort;
    private final LoadCartPort loadPort;

    @Override
    public void clear(ClearCartCommand command) {
        Cart cart = loadPort.loadByUserId(command.userId());
        for(CartDetail cd: cart.getDetails()) {
            variantToUserPort.removeMapping(cd.getVariantId(), cart.getUserId());
        }
        deletePort.clearCart(command);
    }
    
}
