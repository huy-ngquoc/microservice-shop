package vn.uit.edu.msshop.cart.application.dto.command;

import vn.uit.edu.msshop.cart.application.common.Change;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageKey;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantTraits;

public record UpdateCartInfoCommand(UserId userId, VariantId variantId, Change<ImageKey> imageKey , Change<ProductName> name, Change<VariantTraits> traits, Change<UnitPrice> unitPrice) {

}
