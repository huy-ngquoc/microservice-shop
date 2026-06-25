package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantBulkFetchByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.ProductVariant;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantPrice;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductVariantTraits;
import vn.edu.uit.msshop.product.variant.application.dto.query.lookup.VariantProjectionActiveBulkLookupByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.port.in.query.lookup.VariantProjectionActiveBulkLookupByProductIdUseCase;

@Component
@RequiredArgsConstructor
class ProductVariantBulkFetchByProductIdAdapter
        implements ProductVariantBulkFetchByProductIdPort {

    private final VariantProjectionActiveBulkLookupByProductIdUseCase variantProjectionActiveBulkLookupByProductIdUseCase;

    @Override
    public List<ProductVariant> fetchAllActiveByProductId(
            final ProductId productId) {
        final var query = new VariantProjectionActiveBulkLookupByProductIdQuery(productId.value());
        final var views = this.variantProjectionActiveBulkLookupByProductIdUseCase.find(query);
        final var result = new ArrayList<ProductVariant>(views.size());
        for (final var view : views) {
            result.add(new ProductVariant(
                    new ProductVariantId(view.variantId()),
                    new ProductVariantPrice(view.price()),
                    ProductVariantTraits.of(view.traitList())));
        }
        return result;
    }
}
