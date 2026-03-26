package vn.edu.uit.msshop.product.variant.application.port.out;

import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

// TODO: the name of interface is good?
public interface LoadVariantsForProductPort {
    List<Variant> loadByProductId(
            final VariantProductId id);
}
