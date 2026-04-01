package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.RemoveVariantFromProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.UpdateVariantInProductPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantToProductSyncAdapter
        implements UpdateVariantInProductPort,
        RemoveVariantFromProductPort {

    @Override
    public void updateInProduct(
            final Variant variant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInProduct'");
    }

    @Override
    public void removeFromProduct(
            final VariantId id,
            final VariantProductId productId) {

    }
}
