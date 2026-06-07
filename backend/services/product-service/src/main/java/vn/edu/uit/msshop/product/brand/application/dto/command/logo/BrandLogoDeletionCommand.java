package vn.edu.uit.msshop.product.brand.application.dto.command.logo;

import java.util.UUID;

public record BrandLogoDeletionCommand(
        UUID brandId,
        long brandVersion) {
}
