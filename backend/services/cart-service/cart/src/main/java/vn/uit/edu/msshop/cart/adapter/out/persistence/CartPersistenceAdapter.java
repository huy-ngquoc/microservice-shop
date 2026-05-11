package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.out.persistence.mapper.CartDocumentMapper;
import vn.uit.edu.msshop.cart.application.dto.command.ClearCartCommand;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.exception.CartNotFoundException;
import vn.uit.edu.msshop.cart.application.port.out.DeleteCartPort;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.application.port.out.SaveCartPort;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

@Component
@RequiredArgsConstructor
public class CartPersistenceAdapter implements LoadCartPort, SaveCartPort, DeleteCartPort {
    
    private final CartDocumentMapper mapper;
    

    private final CartRepository cartRepo;

    @Override
    
    public Cart loadByUserId(
            UserId userId) {

        CartDocument document = cartRepo.findById(userId.value()).orElse(null);
        if(document==null) return null;
        return mapper.toDomain(document);

    }

    @Override
    
    public Cart save(
            Cart cart) {
        CartDocument document = cartRepo.save(mapper.toDocument(cart));
        return mapper.toDomain(document);
    }

    @Override
    public void deleteCartItem(
            DeleteCartItemCommand command) {
        Cart cart = loadByUserId(command.userId());
        if (cart == null)
            throw new CartNotFoundException(command.userId());
        cart.removeByVariantId(command.variantId());
        save(cart);
    }

    @Override
    
    public void clearCart(
            ClearCartCommand command) {
        Cart cart = loadByUserId(command.userId());
        cartRepo.delete(mapper.toDocument(cart));
    }

    @Override
    public void deleteManyCartItems(
            List<DeleteCartItemCommand> commands) {
        if (commands.isEmpty())
            return;
        UserId userId = commands.get(0).userId();
        Cart cart = loadByUserId(userId);
        if (cart == null)
            throw new CartNotFoundException(userId);
        for (DeleteCartItemCommand command : commands) {
            cart.removeByVariantId(command.variantId());
        }
        save(cart);
    }

}
