package vn.uit.edu.msshop.cart.application.port.out;

import vn.uit.edu.msshop.cart.domain.model.Cart;

public interface SaveCartPort {
    public Cart save(
            Cart cart);
}
