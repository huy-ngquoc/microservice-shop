package vn.edu.uit.msshop.product.product.domain.model;

import java.util.Collection;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductOption;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.shared.domain.Domains;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductConfiguration(
        ProductOptions options,
        ProductVariants variants) {

    public ProductConfiguration {
        Domains.requireNonNull(options, "Options CANNOT be null");
        Domains.requireNonNull(variants, "Variants CANNOT be null");
        ProductConfiguration.validateConsistency(options, variants);
    }

    public ProductConfiguration addOption(
            final ProductOption option,
            final ProductVariantTrait defaultTrait) {
        return new ProductConfiguration(
                this.options.add(option),
                this.variants.appendTraitToAll(defaultTrait));
    }

    public ProductConfiguration removeOptionAt(
            final int index) {
        return new ProductConfiguration(
                this.options.removeAt(index),
                this.variants.removeTraitAt(index));
    }

    public ProductConfiguration replaceOptionAt(
            final int index,
            final ProductOption newOption) {
        return new ProductConfiguration(
                this.options.replaceAt(index, newOption),
                this.variants);
    }

    public ProductConfiguration addVariant(
            final ProductVariant newVariant) {
        return new ProductConfiguration(
                this.options,
                this.variants.add(newVariant));
    }

    public ProductConfiguration removeVariantById(
            final ProductVariantId id) {
        return new ProductConfiguration(
                this.options,
                this.variants.removeById(id));
    }

    public ProductConfiguration replaceVariantById(
            final ProductVariantId id,
            final ProductVariant newVariant) {
        return new ProductConfiguration(
                this.options,
                this.variants.replaceById(id, newVariant));
    }

    public ProductConfiguration addVariants(
            final ProductVariants newVariants) {
        return new ProductConfiguration(
                this.options,
                this.variants.addAll(newVariants));
    }

    public ProductConfiguration removeVariantsByIds(
            final Collection<ProductVariantId> idCollection) {
        return new ProductConfiguration(
                this.options,
                this.variants.removeAllByIds(idCollection));
    }

    private static void validateConsistency(
            final ProductOptions options,
            final ProductVariants variants) {
        if (variants.isEmpty()) {
            throw new DomainException("Product MUST have at least one variant");
        }

        if (options.isEmpty()) {
            if (variants.size() != 1) {
                throw new DomainException("Simple product (no options) must have exactly 1 default variant");
            }

            if (!variants.getValues().getFirst().traits().isEmpty()) {
                throw new DomainException("Default variant must have empty traits");
            }

            return;
        }

        final var expectedCount = options.size();
        for (final var variant : variants.getValues()) {
            if (variant.traits().size() != expectedCount) {
                throw new DomainException(
                        String.format("Variant '%s' provides %d traits, but Product defines %d options",
                                variant.id().value(),
                                variant.traits().size(),
                                expectedCount));
            }
        }
    }
}
