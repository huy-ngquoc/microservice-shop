package vn.uit.edu.msshop.cart.adapter.out.persistence;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisConnection;
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
    private final CartRepository cartRepo;
    
    @Override
    public Cart loadByUserId(UserId userId) {
        String key = KEY_PREFIX+userId.value().toString();
        CartRedisModel result =(CartRedisModel) redisTemplate.opsForValue().get(key);
        if(result==null) {
            CartDocument document = cartRepo.findById(userId.value()).orElse(null);
            if(document==null) return null;
            CartRedisModel redisModel = mapper.toRedisModel(document);
            redisTemplate.opsForValue().set(key, redisModel, CART_LIFE_TIME, TimeUnit.DAYS);
            return mapper.toDomain(redisModel);
        }
        return mapper.toDomain(result); 
    }

    @Override
    public Cart save(Cart cart) {
        long dbSize = redisTemplate.execute(RedisConnection::dbSize);
        if(dbSize>=10000) {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
        }
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

    @Override
    public void deleteManyCartItems(List<DeleteCartItemCommand> commands) {
        if(commands.isEmpty()) return;
        UserId userId = commands.get(0).userId();
        Cart cart = loadByUserId(userId);
        if(cart==null) throw new CartNotFoundException(userId);
        for(DeleteCartItemCommand command: commands) {
            cart.removeByVariantId(command.variantId());
        }
        save(cart);
    }

}
