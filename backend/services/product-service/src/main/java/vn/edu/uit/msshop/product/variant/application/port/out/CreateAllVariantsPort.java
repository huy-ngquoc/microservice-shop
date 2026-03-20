package vn.edu.uit.msshop.product.variant.application.port.out;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.NewVariant;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface CreateAllVariantsPort {
    List<Variant> createAll(
            final Collection<NewVariant> newVariants);
}
