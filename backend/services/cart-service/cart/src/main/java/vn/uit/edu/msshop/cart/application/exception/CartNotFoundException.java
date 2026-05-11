package vn.uit.edu.msshop.cart.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(
            UserId userId) {
        super("Cart not found with user id " + userId.value());
    }
}
