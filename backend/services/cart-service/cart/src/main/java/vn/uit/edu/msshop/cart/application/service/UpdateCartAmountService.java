package vn.uit.edu.msshop.cart.application.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.command.UpdateCartAmountCommand;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.exception.CartNotFoundException;
import vn.uit.edu.msshop.cart.application.mapper.CartViewMapper;
import vn.uit.edu.msshop.cart.application.port.in.UpdateCartAmountUseCase;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.application.port.out.SaveCartPort;
import vn.uit.edu.msshop.cart.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;

@Service
@RequiredArgsConstructor
public class UpdateCartAmountService implements UpdateCartAmountUseCase {
    private final LoadCartPort loadCartPort;
    private final SaveCartPort saveCartPort;
    private final CartViewMapper mapper;

    @Override
    @Transactional
    @CacheEvict(
            cacheNames = CacheNames.CART_BY_USER_ID,
            key = "#command.userId().value()")
    public CartView update(
            UpdateCartAmountCommand command) {
        Cart cart = loadCartPort.loadByUserId(command.userId());
        if (cart == null)
            throw new CartNotFoundException(command.userId());
        final var detail = cart.findByVariantId(command.variantId());
        if (detail == null)
            return null;

        if (detail.getAmount().value() == command.newAmount().apply(detail.getAmount()).value())
            return null;
        final var update = CartDetail.UpdateAmount.builder().amount(command.newAmount().apply(detail.getAmount()))
                .variantId(command.variantId()).build();
        List<CartDetail.UpdateAmount> updateAmounts = List.of(update);
        final var updateAmount = Cart.UpdateAmount.builder().userId(command.userId()).detailUpdateAmounts(updateAmounts)
                .build();
        final var saved = cart.applyUpdateAmount(updateAmount);
        return mapper.toView(saveCartPort.save(saved));
    }

}
