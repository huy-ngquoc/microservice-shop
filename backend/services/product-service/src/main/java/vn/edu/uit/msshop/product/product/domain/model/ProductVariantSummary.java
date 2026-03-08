package vn.edu.uit.msshop.product.product.domain.model;

import java.util.List;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariantSummary(
        ProductVariantId variantId,
        ProductVariantPrice variantPrice,
        List<String> optionValues) {
    public ProductVariantSummary {
        if (variantId == null) {
            throw new DomainException("Variant ID cannot be null in summary");
        }

        if (variantPrice == null) {
            throw new DomainException("Variant price cannot be null in summary");
        }

        if (optionValues == null) {
            optionValues = List.of();
        } else if (optionValues.size() > ProductOptions.MAX_TIERS) {
            throw new DomainException("Variant options amount is too many in summary");
        } else {
            optionValues = optionValues.stream()
                    .map(ProductOption::validateAndNormalizeValue)
                    .toList();
        }
    }
}
