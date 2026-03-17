package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantImageKey;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;

public record UpdateVariantImageCommand(
        VariantId id,
        VariantImageKey newImageKey,
        VariantVersion expectedVersion) {
}
