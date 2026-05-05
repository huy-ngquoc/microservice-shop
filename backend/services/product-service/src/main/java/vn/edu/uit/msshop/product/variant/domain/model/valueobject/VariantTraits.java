package vn.edu.uit.msshop.product.variant.domain.model.valueobject;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record VariantTraits(
        List<VariantTrait> values) {
    public static final int MAX_AMOUNT = 3;

    public VariantTraits {
        if (values == null) {
            throw new DomainException("Variant traits list CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Variant traits list can only have maximum " + MAX_AMOUNT + " traits");
        }

        final var uniqueValues = HashSet.<String>newHashSet(values.size());
        for (final var trait : values) {
            if (trait == null) {
                throw new DomainException("Variant trait CANNOT be null");
            }

            final var lowercaseTrait = trait.value().toLowerCase(Locale.ROOT);
            if (!uniqueValues.add(lowercaseTrait)) {
                throw new DomainException("Duplicate product variant trait found: " + trait.value());
            }
        }

        values = List.copyOf(values);
    }

    public static VariantTraits of(
            final Collection<String> rawStrings) {
        final var traitsList = rawStrings.stream().map(VariantTrait::new).toList();
        return new VariantTraits(traitsList);
    }

    public List<String> unwrap() {
        return this.values.stream().map(VariantTrait::value).toList();
    }

}
