package vn.edu.uit.msshop.product.variant.application.service.command.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import vn.edu.uit.msshop.product.variant.application.exception.VariantsNotFoundException;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

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
}
