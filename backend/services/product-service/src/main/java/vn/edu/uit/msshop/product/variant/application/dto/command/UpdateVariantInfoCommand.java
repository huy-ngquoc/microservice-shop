package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.shared.application.dto.Change;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

public record UpdateVariantInfoCommand(
        VariantId id,
        Change<VariantProductId> productId,
        Change<VariantPrice> price,
        Change<VariantTraits> traits) {
}
