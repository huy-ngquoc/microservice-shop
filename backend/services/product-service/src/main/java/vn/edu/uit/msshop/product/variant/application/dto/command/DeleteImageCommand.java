package vn.edu.uit.msshop.product.variant.application.dto.command;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public record DeleteImageCommand(VariantId variantId, long version) {

}
