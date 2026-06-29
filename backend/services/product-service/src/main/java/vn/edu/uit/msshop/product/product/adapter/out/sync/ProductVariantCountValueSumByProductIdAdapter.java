package vn.edu.uit.msshop.product.product.adapter.out.sync;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantSoldCountSumByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductVariantStockCountSumByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantSoldCountValueSumByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.dto.query.count.VariantStockCountValueSumByProductIdQuery;
import vn.edu.uit.msshop.product.variant.application.port.in.query.count.VariantSoldCountValueSumByProductIdUseCase;
import vn.edu.uit.msshop.product.variant.application.port.in.query.count.VariantStockCountValueSumByProductIdUseCase;

@Component
@RequiredArgsConstructor
class ProductVariantCountValueSumByProductIdAdapter
        implements
        ProductVariantSoldCountSumByProductIdPort,
        ProductVariantStockCountSumByProductIdPort {

    private final VariantSoldCountValueSumByProductIdUseCase soldCountSumUseCase;
    private final VariantStockCountValueSumByProductIdUseCase stockCountSumUseCase;

    @Override
    public int sumSoldByProductId(
            final ProductId productId) {
        final var query = new VariantSoldCountValueSumByProductIdQuery(productId.value());
        return this.soldCountSumUseCase.sum(query);
    }

    @Override
    public int sumStockByProductId(
            final ProductId productId) {
        final var query = new VariantStockCountValueSumByProductIdQuery(productId.value());
        return this.stockCountSumUseCase.sum(query);
    }
}
