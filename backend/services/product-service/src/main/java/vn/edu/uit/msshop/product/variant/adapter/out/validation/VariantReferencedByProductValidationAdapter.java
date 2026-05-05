package vn.edu.uit.msshop.product.variant.adapter.out.validation;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.CheckProductExistsByVariantUseCase;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.variant.application.port.out.validation.CheckVariantReferencedByProductPort;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;

@Component
@RequiredArgsConstructor
public class VariantReferencedByProductValidationAdapter
    implements CheckVariantReferencedByProductPort {
  private final CheckProductExistsByVariantUseCase checkExistsByVariantUseCase;

  @Override
  public boolean isReferencedByProduct(final VariantId variantId) {
    return this.checkExistsByVariantUseCase
        .existsByVariantId(new ProductVariantId(variantId.value()));
  }
}
