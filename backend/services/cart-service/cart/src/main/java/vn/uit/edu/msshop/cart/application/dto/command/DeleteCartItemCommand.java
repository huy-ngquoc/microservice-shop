package vn.uit.edu.msshop.cart.application.dto.command;

import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

public record DeleteCartItemCommand(
        UserId userId,
        VariantId variantId) {

}
