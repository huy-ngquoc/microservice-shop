package vn.edu.uit.msshop.product.variant.domain.model;

import java.util.List;
import vn.edu.uit.msshop.product.shared.domain.exception.DomainException;

public record VariantTraits(
        List<VariantTrait> values) {
    public static final int MAX_TIERS = 3;

    public VariantTraits {
        if (values == null) {
            throw new DomainException("Variant traits list CANNOT be null");
        }

        if (values.size() > MAX_TIERS) {
            throw new DomainException("Variant traits list can only have maximum " + MAX_TIERS + " traits");
        }

        values = List.copyOf(values);
    }
}
