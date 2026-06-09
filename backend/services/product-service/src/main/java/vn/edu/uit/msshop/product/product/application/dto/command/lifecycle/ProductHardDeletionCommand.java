package vn.edu.uit.msshop.product.product.application.dto.command.lifecycle;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVersion;

public record ProductHardDeletionCommand(
        ProductId id,
        ProductVersion expectedVersion) {
}
