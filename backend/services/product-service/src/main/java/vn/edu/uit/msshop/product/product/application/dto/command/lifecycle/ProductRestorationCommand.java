package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record ProductRestorationCommand(
        ProductId id,
        ProductVersion expectedVersion) {
}
