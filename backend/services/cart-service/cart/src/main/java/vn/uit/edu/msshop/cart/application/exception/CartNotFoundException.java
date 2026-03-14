package vn.uit.edu.msshop.cart.application.exception;

import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(UserId userId) {
        super("Cart not found with user id "+userId.value());
    }
}
