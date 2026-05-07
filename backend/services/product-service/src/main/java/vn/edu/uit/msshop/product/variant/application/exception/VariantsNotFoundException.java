package vn.edu.uit.msshop.product.variant.application.exception;

import java.util.Set;
import java.util.stream.Collectors;

import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.shared.application.exception.NotFoundException;

public final class VariantsNotFoundException extends NotFoundException {
  public VariantsNotFoundException(final Set<VariantId> missing) {
    super(Variant.class.getSimpleName(), formatIds(missing));
  }

  private static String formatIds(Set<VariantId> ids) {
    return ids.stream().map(id -> id.value().toString()).collect(Collectors.joining(","));
  }
}
