package vn.uit.edu.msshop.cart.application.dto.command;

import vn.uit.edu.msshop.cart.application.common.Change;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Color;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ImageUrls;
import vn.uit.edu.msshop.cart.domain.model.valueobject.ProductName;
import vn.uit.edu.msshop.cart.domain.model.valueobject.Size;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UnitPrice;
import vn.uit.edu.msshop.cart.domain.model.valueobject.UserId;
import vn.uit.edu.msshop.cart.domain.model.valueobject.VariantId;

public record UpdateCartInfoCommand(UserId userId, VariantId variantId, Change<Color> color, Change<ImageUrls> imageUrls, Change<ProductName> name, Change<Size> size, Change<UnitPrice> unitPrice) {

}
