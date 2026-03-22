package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;

public record SoftDeleteVariantCommand(
        VariantId id,
        VariantVersion expectedVersion) {
}
