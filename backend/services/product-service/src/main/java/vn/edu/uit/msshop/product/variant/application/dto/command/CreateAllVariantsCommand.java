package vn.edu.uit.msshop.product.variant.application.dto.command;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

public record CreateAllVariantsCommand(
        VariantProductId productId,
        List<VariantCommand> variants) {

    public record VariantCommand(
            VariantPrice price,
            VariantTraits traits) {
    }

    public CreateAllVariantsCommand {
        variants = List.copyOf(variants);
    }
}
