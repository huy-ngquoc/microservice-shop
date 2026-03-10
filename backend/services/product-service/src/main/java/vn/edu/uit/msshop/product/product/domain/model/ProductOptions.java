package vn.edu.uit.msshop.product.product.domain.model;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductOptions(
        List<ProductOption> values) {
    public static final int MAX_TIERS = 3;

    private static final ProductOptions EMPTY = new ProductOptions(List.of());

    public ProductOptions {
        if (values == null) {
            throw new DomainException("Product options list CANNOT be null");
        }

        if (values.size() > MAX_TIERS) {
            throw new DomainException("Product can have maximum " + MAX_TIERS + " option tiers");
        }

        final var uniqueNames = HashSet.<String>newHashSet(values.size());
        for (final var option : values) {
            final var lowercaseName = option.name().toLowerCase(Locale.ROOT);
            if (!uniqueNames.add(lowercaseName)) {
                throw new DomainException("Duplicate option name found: " + option.name());
            }
        }

        values = List.copyOf(values);
    }

    public static ProductOptions empty() {
        return ProductOptions.EMPTY;
    }

    public boolean hasOptions() {
        return !values.isEmpty();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int getOptionCount() {
        return values.size();
    }
}
