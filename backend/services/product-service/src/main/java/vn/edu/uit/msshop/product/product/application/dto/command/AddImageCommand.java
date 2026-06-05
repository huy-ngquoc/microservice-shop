package vn.edu.uit.msshop.product.product.application.dto.command;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductImageKey;

public record AddImageCommand(ProductId productId,ProductImageKey key) {

}
