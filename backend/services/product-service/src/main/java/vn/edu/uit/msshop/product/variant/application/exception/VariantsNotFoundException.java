package vn.edu.uit.msshop.product.variant.application.exception;

import java.util.Set;
import java.util.stream.Collectors;

import vn.edu.uit.msshop.shared.application.exception.NotFoundException;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

public final class VariantsNotFoundException extends NotFoundException {
  public VariantsNotFoundException(final Set<VariantId> missing) {
    final var idsString =
        missing.stream().map(id -> id.value().toString()).collect(Collectors.joining(","));

    super(Variant.class.getSimpleName(), idsString);
  }
}
