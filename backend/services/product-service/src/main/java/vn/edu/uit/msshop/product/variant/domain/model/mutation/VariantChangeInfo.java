package vn.edu.uit.msshop.product.variant.domain.model.mutation;

import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;
import vn.edu.uit.msshop.product.variant.domain.model.VariantPrice;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantTraits;

public record VariantChangeInfo(
        VariantProductId productId,
        VariantPrice price,
        VariantTraits traits) {
    public VariantChangeInfo {
        if (productId == null) {
            throw new DomainException("Product ID in Variant change info must NOT be null");
        }

        if (price == null) {
            throw new DomainException("Price in Variant change info must NOT be null");
        }

        if (traits == null) {
            throw new DomainException("Traits in Variant change info must NOT be null");
        }
    }
}
