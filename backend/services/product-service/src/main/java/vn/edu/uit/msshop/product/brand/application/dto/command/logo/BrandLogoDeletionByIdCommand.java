package vn.edu.uit.msshop.product.brand.application.dto.command.logo;

import java.util.UUID;

public record BrandLogoDeletionByIdCommand(
        UUID brandId,
        long brandVersion) {
}
