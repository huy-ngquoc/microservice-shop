package vn.edu.uit.msshop.product.brand.application.dto.command;

import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandLogoKey;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandVersion;

public final class BrandLogoLifecycleCommands {

    public record Update(
            BrandId id,
            BrandLogoKey newLogoKey,
            BrandVersion expectedVersion) {
    }

    public record Delete(
            BrandId id,
            BrandVersion expectedVersion) {
    }
}
