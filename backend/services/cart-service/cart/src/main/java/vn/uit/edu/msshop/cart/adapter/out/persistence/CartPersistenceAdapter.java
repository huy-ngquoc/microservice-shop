package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.cart.adapter.out.persistence.mapper.CartRedisModelMapper;
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
    private final RedisTemplate<String,Object> redisTemplate;
    private final CartRedisModelMapper mapper;
    private static final String KEY_PREFIX="cart_key:";
    private static final int CART_LIFE_TIME=7;
    
    @Override
    public Cart loadByUserId(UserId userId) {
        String key = KEY_PREFIX+userId.value().toString();
        CartRedisModel result =(CartRedisModel) redisTemplate.opsForValue().get(key);
        if(result==null) return null;
        return mapper.toDomain(result); 
    }

    @Override
    public Cart save(Cart cart) {
        String key = KEY_PREFIX + cart.getUserId().value().toString();
        redisTemplate.opsForValue().set(key, mapper.toModel(cart), CART_LIFE_TIME, TimeUnit.DAYS);
        return cart;
    }

    @Override
    public void deleteCartItem(DeleteCartItemCommand command) {
        Cart cart = loadByUserId(command.userId());
        if(cart==null) throw new CartNotFoundException(command.userId());
        cart.removeByVariantId(command.variantId());
        save(cart);
    }

    @Override
    public void clearCart(ClearCartCommand command) {
        redisTemplate.delete(KEY_PREFIX+command.userId().value().toString());
    }

}
