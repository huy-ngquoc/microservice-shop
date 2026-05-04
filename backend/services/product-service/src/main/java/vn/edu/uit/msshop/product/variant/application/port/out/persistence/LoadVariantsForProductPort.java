package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

// TODO: the name of interface is good?
public interface LoadVariantsForProductPort {
    List<Variant> loadAllByProductId(
            final VariantProductId id);
}
