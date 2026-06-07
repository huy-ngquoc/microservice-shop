package vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle;

import java.util.UUID;

public record BrandSoftDeletionCommand(
        UUID brandId,
        long brandVersion) {
}
