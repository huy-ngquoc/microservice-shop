package vn.edu.uit.msshop.product.variant.domain.model;

import org.jspecify.annotations.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.variant.domain.model.mutation.VariantChangeInfo;

@Getter
@EqualsAndHashCode(
        onlyExplicitlyIncluded = true)
@FieldNameConstants
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

    public Variant applyChangeInfo(
            final VariantChangeInfo changeInfo) {
        if (changeInfo == null) {
            throw new DomainException("Variant change info must be null");
        }

        if (this.isInfoUnchanged(changeInfo)) {
            return this;
        }

        return new Variant(this.id,
                changeInfo.productId(),
                this.image,
                changeInfo.price(),
                this.sold,
                changeInfo.traits());
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

    private boolean isInfoUnchanged(
            final VariantChangeInfo changeInfo) {
        return this.productId.equals(changeInfo.productId())
                && this.price.equals(changeInfo.price())
                && this.traits.equals(changeInfo.traits());
    }
}
