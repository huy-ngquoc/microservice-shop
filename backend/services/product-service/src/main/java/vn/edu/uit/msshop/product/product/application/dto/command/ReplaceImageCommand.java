package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKey;

public record ReplaceImageCommand(ProductId productId, ProductImageKey oldKey, ProductImageKey newKey) {

}
