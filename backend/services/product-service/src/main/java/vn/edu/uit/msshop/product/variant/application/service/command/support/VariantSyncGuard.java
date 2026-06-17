package vn.edu.uit.msshop.product.variant.application.service.command.support;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import vn.edu.uit.msshop.product.variant.application.exception.VariantsNotFoundException;
import vn.edu.uit.msshop.product.variant.application.exception.VariantsNotOwnedByProductException;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

public final class VariantSyncGuard {

    private VariantSyncGuard() {
    }

    public static void ensureAllVariantsFound(
            final Set<VariantId> requestedIdSet,
            final Map<VariantId, Variant> loadedById) {
        if (loadedById.size() == requestedIdSet.size()) {
            return;
        }

        final var missing = new HashSet<>(requestedIdSet);
        missing.removeAll(loadedById.keySet());
        throw new VariantsNotFoundException(missing);
    }

    public static void ensureAllBelongToProduct(
            final Collection<Variant> variantList,
            final VariantProductId expectedProductId) {
        final var foreign = variantList.stream()
                .filter(v -> !v.getProductId().equals(expectedProductId))
                .map(Variant::getId)
                .collect(Collectors.toUnmodifiableSet());
        if (!foreign.isEmpty()) {
            throw new VariantsNotOwnedByProductException(expectedProductId, foreign);
        }
    }
}
