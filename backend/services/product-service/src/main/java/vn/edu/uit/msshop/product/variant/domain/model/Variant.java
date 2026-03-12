package vn.edu.uit.msshop.product.variant.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
public final class Variant {
    @EqualsAndHashCode.Include
    private final VariantId id;

    private final VariantProductId productId;

    private final VariantImage image;

    private final VariantPrice price;

    private final VariantSold sold;

    private final VariantTraits traits;

    public Variant(
            VariantId id,
            VariantProductId productId,
            VariantImage image,
            VariantPrice price,
            VariantSold sold,
            VariantTraits traits) {
        this.id = requireNonNull(id, "Variant ID must not be null");
        this.productId = requireNonNull(productId, "Variant Product ID must not be null");
        this.image = requireNonNull(image, "Variant image must not be null");
        this.price = requireNonNull(price, "Variant price must not be null");
        this.sold = requireNonNull(sold, "Variant sold must not be null");
        this.traits = requireNonNull(traits, "Variant traits must not be null");
    }

    private static <T> T requireNonNull(
            @Nullable
            T obj,

            String message) {
        if (obj == null) {
            throw new DomainException(message);
        }

        return obj;
    }
}
