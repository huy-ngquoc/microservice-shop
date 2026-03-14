package vn.uit.edu.msshop.cart.adapter.out.persistence.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.cart.adapter.out.persistence.CartItemRedisModel;
import vn.uit.edu.msshop.cart.adapter.out.persistence.CartRedisModel;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Color;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageUrls;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Size;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

@Component
public class CartRedisModelMapper {
    private CartDetail.Draft toDraft(CartItemRedisModel model) {
        return CartDetail.Draft.builder().variantId(new VariantId(UUID.fromString(model.getVariantId())))
        .imageUrls(new ImageUrls(model.getImageUrls()))
        .name(new ProductName(model.getName()))
        .amount(new Amount(model.getAmount()))
        .price(new UnitPrice(model.getPrice().longValue()))
        .color(new Color(model.getColor()))
        .size(new Size(model.getSize()))
        .build();
    }
    
    public Cart toDomain(CartRedisModel model) {
        final var draft = Cart.Draft.builder().userId(new UserId(UUID.fromString(model.getUserId())))
        .detailDrafts(model.getItems().stream().map(item->toDraft(item)).toList()).build();
        return Cart.create(draft);
    }
    public CartItemRedisModel toModel(CartDetail domain) {
        return new CartItemRedisModel(domain.getVariantId().value().toString(), domain.getImageUrls().images(), domain.getName().value(), BigDecimal.valueOf(domain.getPrice().value()), domain.getColor().value(), domain.getSize().value(), domain.getAmount().value());
    }
    public CartRedisModel toModel(Cart cart){
        return new CartRedisModel(cart.getUserId().value().toString(), cart.getDetails().stream().map(this::toModel).toList());
    }
}
