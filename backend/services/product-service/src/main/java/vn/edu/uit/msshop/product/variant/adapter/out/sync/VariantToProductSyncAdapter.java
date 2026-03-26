package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.application.port.out.RemoveVariantFromProductPort;
import vn.edu.uit.msshop.product.variant.application.port.out.UpdateVariantInProductPort;
import vn.edu.uit.msshop.product.variant.domain.model.Variant;
import vn.edu.uit.msshop.product.variant.domain.model.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.VariantProductId;

@Component
@RequiredArgsConstructor
public class VariantToProductSyncAdapter
        implements UpdateVariantInProductPort,
        RemoveVariantFromProductPort {

    @Override
    public void updateInProduct(
            Variant variant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInProduct'");
    }

    @Override
    public void removeFromProduct(
            VariantId id,
            VariantProductId productId) {

    }
}
