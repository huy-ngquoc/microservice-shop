package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.BrandVersion;

public record UpdateBrandLogoCommand(
        BrandId id,
        BrandLogoKey newLogoKey,
        BrandVersion expectedVersion) {
}
