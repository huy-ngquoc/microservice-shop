package vn.uit.edu.msshop.cart.application.dto.command;

import java.util.List;

import vn.uit.edu.msshop.cart.domain.model.CartDetail;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

public record CreateCartCommand(
        UserId userId,
        List<CartDetail> details) {

}
