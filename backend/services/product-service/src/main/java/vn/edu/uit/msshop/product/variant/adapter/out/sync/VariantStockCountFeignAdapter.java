package vn.edu.uit.msshop.product.variant.adapter.out.sync;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.request.VariantStockCountBulkFetchRequest;
import vn.edu.uit.msshop.product.variant.adapter.out.sync.response.VariantStockCountResponse;
import vn.edu.uit.msshop.product.variant.application.port.out.sync.VariantStockCountBulkFetchPort;
import vn.edu.uit.msshop.product.variant.domain.model.sync.VariantInventoryStockCount;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantId;
import vn.edu.uit.msshop.product.variant.domain.model.valueobject.VariantStockCountValue;

@Component
@RequiredArgsConstructor
public class VariantStockCountFeignAdapter
        implements VariantStockCountBulkFetchPort {

    private static final int PAGE_SIZE = 100;

    private final VariantStockCountFeignClient feignClient;

    @Override
    public List<VariantInventoryStockCount> fetchAll(
            final Instant rangeStartTime,
            final Instant rangeEndTime) {
        final var allStockCounts = new ArrayList<VariantInventoryStockCount>();

        int pageNumber = 0;
        boolean hasNextPage = false;

        final var request = new VariantStockCountBulkFetchRequest(
                rangeStartTime,
                rangeEndTime,
                rangeStartTime,
                rangeEndTime);

        do {
            final var pageResponse = this.feignClient.getStockCounts(
                    request,
                    pageNumber,
                    PAGE_SIZE);

            final var mappedItems = pageResponse.items().stream()
                    .map(VariantStockCountFeignAdapter::toDomainDto)
                    .toList();

            allStockCounts.addAll(mappedItems);
            hasNextPage = pageResponse.hasNext();
            pageNumber++;

        } while (hasNextPage);

        return List.copyOf(allStockCounts);
    }

    private static VariantInventoryStockCount toDomainDto(
            final VariantStockCountResponse response) {
        return new VariantInventoryStockCount(
                new VariantId(response.variantId()),
                new VariantStockCountValue(response.quantity()));
    }
}
