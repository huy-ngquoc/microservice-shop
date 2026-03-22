package vn.edu.uit.msshop.product.product.domain.model;

import java.util.Collection;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record NewProductVariant(
        ProductVariantPrice price,
        ProductVariantTraits traits) {
    public NewProductVariant {
        if (price == null) {
            throw new DomainException("Variant price cannot be null");
        }

        if (traits == null) {
            throw new DomainException("Variant traits cannot be null");
        }
    }

    public static NewProductVariant of(
            final long price,
            final Collection<String> traits) {
        return new NewProductVariant(
                new ProductVariantPrice(price),
                ProductVariantTraits.of(traits));
    }
}
