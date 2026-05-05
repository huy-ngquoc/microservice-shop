package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import java.util.Collection;
import java.util.List;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;

public interface UpdateAllVariantsPort {
  List<Variant> updateAll(final Collection<Variant> variants);
}
