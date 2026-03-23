package vn.uit.edu.msshop.cart.application.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.CreateCartCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.mapper.CartViewMapper;
import vn.uit.edu.msshop.cart.application.port.in.CreateCartUseCase;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.application.port.out.SaveCartPort;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;

@Service
@RequiredArgsConstructor
public class CreateCartService implements CreateCartUseCase {
    private final LoadCartPort loadCartPort;
    private final SaveCartPort savePort;
    private final CartViewMapper mapper;
    private final VariantToUserPort variantToUserPort;

    @Override
    public CartView create(CreateCartCommand command) {
        Cart cart = loadCartPort.loadByUserId(command.userId());
        if(cart==null) {
        
            cart=Cart.createEmpty(command.userId());
        }
        cart = cart.addItems(command.details());
        for(CartDetail cd: command.details()) {
            variantToUserPort.addMapping(cd.getVariantId(), command.userId());
        }
        return mapper.toView(savePort.save(cart));
    }
}
