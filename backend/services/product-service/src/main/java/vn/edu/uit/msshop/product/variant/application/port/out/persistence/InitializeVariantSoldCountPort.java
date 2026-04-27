package vn.edu.uit.msshop.product.variant.application.port.out.persistence;

import vn.edu.uit.msshop.product.variant.domain.model.VariantSoldCount;
import vn.edu.uit.msshop.product.variant.domain.model.creation.NewVariantSoldCount;

public interface InitializeVariantSoldCountPort {
    VariantSoldCount initialize(
            final NewVariantSoldCount newSoldCount);
}
