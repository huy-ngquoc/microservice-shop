package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;
import vn.edu.uit.msshop.product.variant.domain.model.VariantVersion;

public record UpdateVariantInfoCommand(
        VariantId id,
        Change<VariantPrice> price,
        Change<VariantTraits> traits,
        VariantVersion expectedVersion) {
}
