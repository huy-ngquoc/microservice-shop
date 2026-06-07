package vn.edu.uit.msshop.product.brand.application.dto.command.logo;

import java.util.UUID;

public record BrandLogoUpdateCommand(
        UUID brandId,
        String brandNewLogoKey,
        long brandVersion) {
}
