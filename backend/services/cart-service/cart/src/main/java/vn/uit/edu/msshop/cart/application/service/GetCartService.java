package vn.uit.edu.msshop.cart.application.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.application.exception.CartNotFoundException;
import vn.uit.edu.msshop.cart.application.mapper.CartViewMapper;
import vn.uit.edu.msshop.cart.application.port.in.GetCartUseCase;
import vn.uit.edu.msshop.cart.application.port.out.LoadCartPort;
import vn.uit.edu.msshop.cart.bootstrap.config.cache.CacheNames;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

@Service
@RequiredArgsConstructor
public class GetCartService implements GetCartUseCase {
    private final LoadCartPort loadPort;
    private final CartViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            value = CacheNames.CART_BY_USER_ID,
            key = "#userId.value()")
    public CartView getByUserId(
            UserId userId) {
        final var cart = loadPort.loadByUserId(userId);
        if (cart == null) {
            throw new CartNotFoundException(userId);
        }
        return mapper.toView(cart);
    }
}
