package vn.edu.uit.msshop.product.product.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductPriceRange;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTrait;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.shared.domain.exception.DomainException;

public record ProductVariants(
        List<ProductVariant> values) {

    public static final int MAX_AMOUNT = 50;

    public ProductVariants {
        if (values == null) {
            throw new DomainException("Variants CANNOT be null");
        }

        if (values.size() > MAX_AMOUNT) {
            throw new DomainException("Variants CANNOT exceed " + MAX_AMOUNT);
        }

        final var uniqueIdSet = HashSet.<ProductVariantId>newHashSet(values.size());
        final var uniqueCombinationSet = HashSet.<List<String>>newHashSet(values.size());

        for (final var variant : values) {
            if (variant == null) {
                throw new DomainException("Variant in list CANNOT be null");
            }

            if (!uniqueIdSet.add(variant.id())) {
                throw new DomainException("Duplicate variant ID found: " + variant.id().value());
            }

            final var normalizedTraitValues = variant.traits().unwrapNormalized();
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

    public ProductVariants removeAllByIds(
            final Collection<ProductVariantId> idCollection) {
        if (idCollection.isEmpty()) {
            return this;
        }

        var newVariants = this;
        for (final var id : idCollection) {
            newVariants = newVariants.removeById(id);
        }

        return newVariants;
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
            final ProductVariantTrait trait) {
        final var newValues = this.values.stream()
                .map(v -> new ProductVariant(
                        v.id(),
                        v.price(),
                        v.traits().add(trait)))
                .toList();
        return new ProductVariants(newValues);
    }

    public boolean collapsesOnTraitRemovalAt(
            final int traitIndex) {
        final var seen = HashSet.<List<String>>newHashSet(this.values.size());
        for (final var variant : this.values) {
            final var normalizedRawTraitList = new ArrayList<>(
                    variant.traits().unwrapNormalized());

            if ((traitIndex < 0) || (traitIndex >= normalizedRawTraitList.size())) {
                throw new DomainException("Trait index out of bounds: " + traitIndex);
            }
            normalizedRawTraitList.remove(traitIndex);

            if (!seen.add(normalizedRawTraitList)) {
                return true;
            }
        }
        return false;
    }

    public boolean appendTraitCollides(
            final ProductVariantTrait trait) {
        final var normalizedNewValue = trait.value().toLowerCase(Locale.ROOT);
        for (final var variant : this.values) {
            if (variant.traits().unwrapNormalized().contains(normalizedNewValue)) {
                return true;
            }
        }
        return false;
    }

    public boolean combinationExists(
            final ProductVariantTraits traits) {
        final var target = traits.unwrapNormalized();
        for (final var variant : this.values) {
            if (variant.traits().unwrapNormalized().equals(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAllIds(
            final Collection<ProductVariantId> ids) {
        final var presentIds = HashSet.<ProductVariantId>newHashSet(this.values.size());
        for (final var variant : this.values) {
            presentIds.add(variant.id());
        }
        return presentIds.containsAll(ids);
    }

    public boolean removingByIdsLeavesEmpty(
            final Collection<ProductVariantId> idCollection) {
        final var idSetToRemove = Set.copyOf(idCollection);
        final var remaining = this.values.stream()
                .filter(variant -> !idSetToRemove.contains(variant.id()))
                .count();
        return remaining == 0;
    }
}
