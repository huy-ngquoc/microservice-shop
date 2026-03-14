package vn.uit.edu.msshop.cart.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;
import vn.uit.edu.msshop.cart.application.port.in.ClearCartUseCase;
import vn.uit.edu.msshop.cart.application.port.out.DeleteCartPort;

@Service
@RequiredArgsConstructor
public class ClearCartService implements ClearCartUseCase {
    private final DeleteCartPort deletePort;

    @Override
    public void clear(ClearCartCommand command) {
        deletePort.clearCart(command);
    }
    
}
