package vn.edu.uit.msshop.product.product.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariants(
        List<ProductVariantSummary> values) {
    public static final int MAX_AMOUNT = ProductOptions.MAX_TIERS * ProductOption.MAX_AMOUNT_VALUES;

    public ProductVariants {
        if (values == null) {
            throw new DomainException("Variants CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Variants CANNOT exceed " + MAX_AMOUNT);
        }

        final var uniqueCombinationSet = HashSet.<String>newHashSet(values.size());
        final var processedVariants = new ArrayList<ProductVariantSummary>(values.size());

        for (final var variant : values) {
            if (variant == null) {
                throw new DomainException("Variant summary in list CANNOT be null");
            }

            final var normalizedOptionValues = variant.optionValues().stream()
                    .map(String::toLowerCase)
                    .toList();

            final var combinationSignature = String.join("|", normalizedOptionValues);
            if (!uniqueCombinationSet.add(combinationSignature)) {
                throw new DomainException("Duplicate variant option combination found: " + variant.optionValues());
            }

            processedVariants.add(variant);
        }

        values = List.copyOf(processedVariants);
    }

    public static ProductVariants empty() {
        return new ProductVariants(List.of());
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public int getCount() {
        return this.values.size();
    }
}
