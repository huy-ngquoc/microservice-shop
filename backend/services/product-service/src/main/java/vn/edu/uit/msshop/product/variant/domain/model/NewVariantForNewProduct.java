package vn.edu.uit.msshop.product.variant.domain.model;

import java.util.Collection;

import vn.edu.uit.msshop.product.shared.domain.Domains;

public record NewVariantForNewProduct(
        VariantPrice price,
        VariantTraits traits) {
    public NewVariantForNewProduct {
        Domains.requireNonNull(price, "Variant price must not be null");
        Domains.requireNonNull(traits, "Variant traits must not be null");
    }

    public static NewVariantForNewProduct of(
            final long price,
            final Collection<String> traits) {
        return new NewVariantForNewProduct(
                new VariantPrice(price),
                VariantTraits.of(traits));
    }
}
