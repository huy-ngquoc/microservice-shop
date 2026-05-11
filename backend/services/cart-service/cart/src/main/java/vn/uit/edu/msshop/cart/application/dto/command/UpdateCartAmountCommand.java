package vn.uit.edu.msshop.cart.application.dto.command;

import vn.uit.edu.msshop.cart.application.common.Change;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Amount;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;
public record UpdateCartAmountCommand(
        UserId userId,
        VariantId variantId,
        Change<Amount> newAmount) {

}
