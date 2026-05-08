package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

public record SoftDeleteBrandCommand(
        BrandId id,
        BrandVersion expectedVersion) {
}
