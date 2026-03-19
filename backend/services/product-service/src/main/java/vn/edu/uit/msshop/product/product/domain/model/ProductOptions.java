package vn.edu.uit.msshop.product.product.domain.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductOptions(
        List<ProductOption> values) {
    public static final int MAX_AMOUNT = 3;

    private static final ProductOptions EMPTY = new ProductOptions(List.of());

    public ProductOptions {
        if (values == null) {
            throw new DomainException("Product options list CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Product can have maximum " + MAX_AMOUNT + " options");
        }

        final var uniqueNames = HashSet.<String>newHashSet(values.size());
        for (final var option : values) {
            final var lowercaseName = option.value().toLowerCase(Locale.ROOT);
            if (!uniqueNames.add(lowercaseName)) {
                throw new DomainException("Duplicate option value found: " + option.value());
            }
        }

        values = List.copyOf(values);
    }

    public static ProductOptions of(
            final Collection<String> rawOptionsList) {
        final var optionsList = rawOptionsList.stream().map(ProductOption::new).toList();
        return new ProductOptions(optionsList);
    }

    public List<String> unwrap() {
        return this.values.stream().map(ProductOption::value).toList();
    }

    public static ProductOptions empty() {
        return ProductOptions.EMPTY;
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }
}
