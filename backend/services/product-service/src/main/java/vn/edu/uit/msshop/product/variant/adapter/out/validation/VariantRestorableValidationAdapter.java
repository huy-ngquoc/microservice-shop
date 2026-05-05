package vn.edu.uit.msshop.product.variant.adapter.out.validation;

import java.util.Locale;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindProductUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.exception.BusinessRuleException;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckVariantRestorablePort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;

@Component
@RequiredArgsConstructor
public class VariantRestorableValidationAdapter implements CheckVariantRestorablePort {
  private final FindProductUseCase findProductUseCase;

  // TODO: too long, split into smaller function
  @Override
  public void validateRestorable(final Variant variant) {
    final var productId = new ProductId(variant.getProductId().value());
    final var product = this.findProductUseCase.findById(productId);

    final var optionsCount = product.options().size();
    final var traitsCount = variant.getTraits().values().size();

    if (traitsCount != optionsCount) {
      final var message = String.format(
          "Cannot restore variant: "
              + "traits count (%d) does not match product options count (%d)",
          traitsCount, optionsCount);
      throw new BusinessRuleException(message);
    }

    final var variantTraitsNormalized =
        variant.getTraits().values().stream().map(t -> t.value().toLowerCase(Locale.ROOT)).toList();

    final var duplicateExists = product.variants().stream().anyMatch(v -> {
      final var existingNormalized = v.traits().stream().map(String::toLowerCase).toList();
      return existingNormalized.equals(variantTraitsNormalized);
    });

    if (duplicateExists) {
      throw new BusinessRuleException(
          "Cannot restore variant: trait combination already exists in product");
    }
  }

}
