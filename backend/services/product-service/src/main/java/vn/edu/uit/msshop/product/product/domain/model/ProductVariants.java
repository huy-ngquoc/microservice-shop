package vn.edu.uit.msshop.product.product.domain.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record ProductVariants(
        List<ProductVariant> values) {
    public static final int MAX_AMOUNT = Math.powExact(
            ProductOptions.MAX_AMOUNT,
            ProductVariantTraits.MAX_TRAITS_AMOUNT);

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

    public ProductPriceRange getPriceRange() {
        if (this.values.isEmpty()) {
            return ProductPriceRange.zero();
        }

        var min = Long.MAX_VALUE;
        var max = Long.MIN_VALUE;
        for (final var variant : this.values) {
            final var price = variant.price().value();
            min = Math.min(min, price);
            max = Math.max(max, price);
        }
        return new ProductPriceRange(new ProductPrice(min), new ProductPrice(max));
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public int size() {
        return this.values.size();
    }

    public ProductVariants addAll(
            final ProductVariants variants) {
        final var newValues = new ArrayList<>(this.values);
        newValues.addAll(variants.values());
        return new ProductVariants(newValues);
    }

    public ProductVariants add(
            final ProductVariant variant) {
        final var newValues = new ArrayList<>(this.values);
        newValues.add(variant);
        return new ProductVariants(newValues);
    }

    public ProductVariants removeById(
            final ProductVariantId variantId) {
        final var newValues = this.values.stream()
                .filter(v -> !v.id().equals(variantId))
                .toList();
        if (newValues.size() == this.values.size()) {
            throw new DomainException("Variant not found: " + variantId.value());
        }
        return new ProductVariants(newValues);
    }

    public Optional<ProductVariant> findById(
            final ProductVariantId variantId) {
        return this.values.stream()
                .filter(v -> v.id().equals(variantId))
                .findFirst();
    }

    public ProductVariants removeTraitAt(
            final int optionIndex) {
        final var newValues = this.values.stream()
                .map(v -> new ProductVariant(
                        v.id(),
                        v.price(),
                        v.traits().removeAt(optionIndex)))
                .toList();

        return new ProductVariants(newValues);
    }

    // TODO: do we need id as param?
    public ProductVariants replaceById(
            final ProductVariantId id,
            final ProductVariant newVariant) {
        final var newValues = this.values.stream()
                .map(v -> v.id().equals(id) ? newVariant : v)
                .toList();
        return new ProductVariants(newValues);
    }

    public ProductVariants appendTraitToAll(
            final Map<ProductVariantId, ProductVariantTrait> traitAssignments) {
        final var newValues = this.values.stream()
                .map(variant -> ProductVariants.appendAssignedTrait(variant, traitAssignments))
                .toList();
        return new ProductVariants(newValues);
    }

    private static ProductVariant appendAssignedTrait(
            final ProductVariant variant,
            final Map<ProductVariantId, ProductVariantTrait> traitAssignments) {
        final var trait = traitAssignments.get(variant.id());
        if (trait == null) {
            throw new DomainException(
                    "Missing trait assignment for variant: " + variant.id().value());
        }

        return new ProductVariant(
                variant.id(),
                variant.price(),
                variant.traits().add(trait));
    }
}
