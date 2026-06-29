package vn.edu.uit.msshop.product.product.adapter.out.sync;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantSoldCountSumByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantStockCountSumByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantSoldCountSumByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantStockCountSumByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.port.in.query.count.VariantSoldCountSumByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.count.VariantStockCountSumByProductIdUseCase;

@Component
@RequiredArgsConstructor
class ProductVariantCountValueSumByProductIdAdapter
        implements
        ProductVariantSoldCountSumByProductIdPort,
        ProductVariantStockCountSumByProductIdPort {

    private final VariantSoldCountSumByProductIdUseCase soldCountSumUseCase;
    private final VariantStockCountSumByProductIdUseCase stockCountSumUseCase;

    @Override
    public int sumSoldByProductId(
            final ProductId productId) {
        final var query = new VariantSoldCountSumByProductIdQuery(productId.value());
        return this.soldCountSumUseCase.sum(query);
    }

    @Override
    public int sumStockByProductId(
            final ProductId productId) {
        final var query = new VariantStockCountSumByProductIdQuery(productId.value());
        return this.stockCountSumUseCase.sum(query);
    }
}
