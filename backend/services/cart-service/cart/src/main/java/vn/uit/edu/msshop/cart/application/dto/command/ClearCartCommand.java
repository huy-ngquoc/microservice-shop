package vn.uit.edu.msshop.cart.application.dto.command;

import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;

public record ClearCartCommand(
        UserId userId) {

}
