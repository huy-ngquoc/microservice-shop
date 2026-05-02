package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.StockCountResponse;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.FetchInventoryStockCountsPort;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Component
@RequiredArgsConstructor
public class InventoryStockCountFeignAdapter
        implements FetchInventoryStockCountsPort {
    private final InventoryStockCountFeignClient feignClient;

    @Override
    public List<VariantInventoryStockCount> fetchAll() {
        return this.feignClient.getStockCounts().stream()
                .map(InventoryStockCountFeignAdapter::toDomainDto)
                .toList();
    }

    private static VariantInventoryStockCount toDomainDto(
            final StockCountResponse response) {
        return new VariantInventoryStockCount(
                new VariantId(response.variantId()),
                new VariantStockCountValue(response.value()));
    }
}
