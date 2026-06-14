package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface VariantBulkUpdatePort {
    List<Variant> updateAll(
            final Collection<Variant> variants);
}
