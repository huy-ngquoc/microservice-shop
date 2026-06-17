package vn.edu.uit.msshop.product.variant.application.exception;

import java.util.Set;
import java.util.stream.Collectors;

import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;

public final class VariantsNotOwnedByProductException
        extends BusinessRuleException {
    public VariantsNotOwnedByProductException(
            final VariantProductId expectedProductId,
            final Set<VariantId> foreignVariantIds) {
        super(VariantsNotOwnedByProductException.formatMessage(
                expectedProductId,
                foreignVariantIds));
    }

    private static String formatMessage(
            final VariantProductId expectedProductId,
            final Set<VariantId> foreignVariantIds) {
        final var ids = foreignVariantIds.stream()
                .map(id -> id.value().toString())
                .collect(Collectors.joining(","));
        return "Variants [" + ids + "] do not belong to product "
                + expectedProductId.value();
    }
}
