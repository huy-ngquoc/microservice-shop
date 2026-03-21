package vn.edu.uit.msshop.product.product.domain.model;

import vn.edu.uit.msshop.product.shared.domain.Domains;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record NewProductConfiguration(
        ProductOptions options,
        NewProductVariants newVariants) {
    public NewProductConfiguration {
        Domains.requireNonNull(options, "Options CANNOT be null");
        Domains.requireNonNull(newVariants, "Variants CANNOT be null");
        NewProductConfiguration.validateConsistency(options, newVariants);
    }

    private static void validateConsistency(
            final ProductOptions options,
            final NewProductVariants newVariants) {
        if (newVariants.isEmpty()) {
            throw new DomainException("Product MUST have at least one variant");
        }

        if (options.isEmpty()) {
            if (newVariants.size() != 1) {
                throw new DomainException(
                        "Simple product (no options) must have exactly 1 default variant");
            }

            if (!newVariants.values().getFirst().traits().isEmpty()) {
                throw new DomainException("Default variant must have empty traits");
            }

            return;
        }

        final var expectedCount = options.size();
        for (final var variant : newVariants.values()) {
            if (variant.traits().size() != expectedCount) {
                throw new DomainException(String.format(
                        "Variant '%s' provides %d traits, but Product defines %d options",
                        variant.traits().values(), variant.traits().size(), expectedCount));
            }
        }
    }
}
