package vn.edu.uit.msshop.product.product.domain.model.valueobject;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantTargets;

public record ProductVariantTargets(
        List<ProductVariantTarget> values) {
    public static final int MAX_AMOUNT = VariantTargets.MAX_AMOUNT;
    private static final ProductVariantTargets EMPTY = new ProductVariantTargets(List.of());

    public ProductVariantTargets {
        if (values == null) {
            throw new DomainException("Variant targets list CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Variant targets list can only have maximum " + MAX_AMOUNT + " targets");
        }

        final var uniqueValues = HashSet.<String>newHashSet(values.size());
        for (final var trait : values) {
            if (trait == null) {
                throw new DomainException("Variant target CANNOT be null");
            }

            final var lowercaseTrait = trait.value().toLowerCase(Locale.ROOT);
            if (!uniqueValues.add(lowercaseTrait)) {
                throw new DomainException("Duplicate product variant target found: " + trait.value());
            }
        }

        values = List.copyOf(values);
    }

    public static ProductVariantTargets of(
            final Collection<String> rawStrings) {
        final var traitsList = rawStrings.stream().map(ProductVariantTarget::new).toList();
        return new ProductVariantTargets(traitsList);
    }

    public static ProductVariantTargets empty() {
        return ProductVariantTargets.EMPTY;
    }

    public List<String> unwrap() {
        return this.values.stream().map(ProductVariantTarget::value).toList();
    }
}
