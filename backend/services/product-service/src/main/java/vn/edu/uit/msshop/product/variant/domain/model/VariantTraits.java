package vn.edu.uit.msshop.product.variant.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantTraits(
        List<VariantTrait> values) {
    public static final int MAX_TIERS = 3;

    public VariantTraits {
        if (values == null) {
            throw new DomainException("Variant traits list CANNOT be null");
        }

        if (values.size() > MAX_TIERS) {
            throw new DomainException("Variant traits list can only have maximum " + MAX_TIERS + " traits");
        }

        final var uniqueNames = HashSet.<String>newHashSet(values.size());
        for (final var option : values) {
            final var lowercaseName = option.name().toLowerCase(Locale.ROOT);
            if (!uniqueNames.add(lowercaseName)) {
                throw new DomainException("Duplicate trait name found: " + option.name());
            }
        }

        values = List.copyOf(values);
    }
}
