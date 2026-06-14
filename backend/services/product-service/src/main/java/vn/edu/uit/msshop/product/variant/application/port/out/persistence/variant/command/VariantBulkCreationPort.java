package vn.edu.uit.msshop.product.variant.application.port.out.persistence.variant.command;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariant;

public interface VariantBulkCreationPort {
    List<Variant> createAll(
            final Collection<NewVariant> newVariants);
}
