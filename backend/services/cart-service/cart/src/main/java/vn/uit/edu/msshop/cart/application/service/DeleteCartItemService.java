package vn.uit.edu.msshop.cart.application.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.DeleteCartItemCommand;
import vn.uit.edu.msshop.cart.application.port.in.DeleteCartItemUseCase;
import vn.uit.edu.msshop.cart.application.port.out.DeleteCartPort;
import vn.uit.edu.msshop.cart.application.port.out.VariantToUserPort;
import vn.uit.edu.msshop.cart.bootstrap.config.cache.CacheNames;

@Service
@RequiredArgsConstructor
public class DeleteCartItemService implements DeleteCartItemUseCase {
    private final DeleteCartPort deletePort;
    private final VariantToUserPort variantToUserPort;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CART_BY_USER_ID,
            key = "#command.userId().value()")
    public void deleteCartItem(
            DeleteCartItemCommand command) {
        variantToUserPort.removeMapping(command.variantId(), command.userId());
        deletePort.deleteCartItem(command);
    }

    @Override
    @CacheEvict(
            cacheNames = CacheNames.CART_BY_USER_ID,
            allEntries = true)
    public void deleteManyItems(
            List<DeleteCartItemCommand> commands) {
        for (DeleteCartItemCommand command : commands) {
            variantToUserPort.removeMapping(command.variantId(), command.userId());
        }
        deletePort.deleteManyCartItems(commands);
    }
}
