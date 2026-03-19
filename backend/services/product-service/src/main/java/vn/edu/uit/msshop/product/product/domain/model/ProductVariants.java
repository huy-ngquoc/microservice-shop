package vn.edu.uit.msshop.product.product.domain.model;

import java.util.HashSet;
import java.util.List;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariants(
        List<ProductVariant> values) {
    public static final int MAX_AMOUNT = ProductOptions.MAX_AMOUNT * ProductVariantTraits.MAX_TRAITS_AMOUNT;

    public ProductVariants {
        if (values == null) {
            throw new DomainException("Variants CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Variants CANNOT exceed " + MAX_AMOUNT);
        }

        final var uniqueCombinationSet = HashSet.<List<String>>newHashSet(values.size());

        for (final var variant : values) {
            if (variant == null) {
                throw new DomainException("Variant in list CANNOT be null");
            }

            final var normalizedTraitValues = variant.traits().values()
                    .stream()
                    .map(ProductVariantTrait::value)
                    .map(String::toLowerCase)
                    .toList();

            if (!uniqueCombinationSet.add(normalizedTraitValues)) {
                throw new DomainException("Duplicate variant traits combination found: " + variant.traits());
            }
        }

        values = List.copyOf(values);
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public int size() {
        return this.values.size();
    }
}
