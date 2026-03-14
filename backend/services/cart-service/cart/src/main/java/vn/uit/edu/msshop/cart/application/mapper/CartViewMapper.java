package vn.uit.edu.msshop.cart.application.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import vn.uit.edu.msshop.cart.application.dto.query.CartDetailView;
import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.CartDetail;

@Component
public class CartViewMapper {
    public CartDetailView toView(CartDetail cartDetail) {
        return new CartDetailView(cartDetail.getVariantId().value(), cartDetail.getImageUrls().images(), cartDetail.getColor().value(), cartDetail.getName().value(), cartDetail.getSize().value(), cartDetail.getPrice().value(), cartDetail.getAmount().value());
    }
    public CartView toView(Cart cart) {
        CartView result=  new CartView(cart.getUserId().value(),new ArrayList<>());
        for(CartDetail detail:cart.getDetails()) {
            result.getDetailViews().add(toView(detail));
        }
        return result;
    }
}
