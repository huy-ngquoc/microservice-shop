package vn.uit.edu.msshop.cart.application.port.out;

import vn.uit.edu.msshop.cart.domain.model.Cart;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

public interface LoadCartPort {
    public Cart loadByUserId(
            UserId userId);
};
