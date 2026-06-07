package vn.edu.uit.msshop.product.brand.application.dto.command.lifecycle;

import java.util.UUID;

public record BrandHardDeletionCommand(
        UUID brandId,
        long brandVersion) {
}
