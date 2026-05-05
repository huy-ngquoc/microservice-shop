package vn.edu.uit.msshop.product.variant.application.mapper;

import org.springframework.stereotype.Component;

import vn.edu.uit.msshop.product.variant.application.dto.view.VariantImageView;
import vn.edu.uit.msshop.product.variant.application.dto.view.VariantView;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.VariantStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantImageKey;

@Component
public class VariantViewMapper {
  public VariantView toView(final Variant variant, final VariantSoldCount soldCount,
      final VariantStockCount stockCount) {
    final var traits = variant.getTraits().unwrap();
    final var targets = variant.getTargets().unwrap();

    return new VariantView(variant.getId().value(), variant.getProductId().value(),
        variant.getProductName().value(), variant.getPrice().value(), soldCount.getValue().value(),
        stockCount.getValue().value(), traits, targets,
        VariantImageKey.unwrap(variant.getImageKey()), variant.getVersion().value());
  }

  public VariantImageView toImageView(final Variant variant) {
    return new VariantImageView(variant.getId().value(),
        VariantImageKey.unwrap(variant.getImageKey()), variant.getVersion().value());
  }
}
