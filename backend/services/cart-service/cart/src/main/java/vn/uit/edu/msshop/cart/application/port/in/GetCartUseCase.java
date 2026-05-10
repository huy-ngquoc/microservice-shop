package vn.uit.edu.msshop.cart.application.port.in;

import vn.uit.edu.msshop.cart.application.dto.query.CartView;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

public interface GetCartUseCase {
    public CartView getByUserId(
            UserId userId);
}
