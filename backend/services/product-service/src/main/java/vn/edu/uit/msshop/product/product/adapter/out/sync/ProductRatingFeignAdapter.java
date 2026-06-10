package vn.edu.uit.msshop.product.product.adapter.out.sync;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.adapter.out.sync.response.RatingInfoResponse;
import vn.edu.uit.msshop.product.product.application.port.out.sync.ProductRatingBulkFetchPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingTotal;
import vn.edu.uit.msshop.product.product.domain.sync.ProductRatingSnapshot;

@Component
@RequiredArgsConstructor
public class ProductRatingFeignAdapter
        implements ProductRatingBulkFetchPort {
    private static final int PAGE_SIZE = 100;

    private final ProductRatingFeignClient feignClient;

    @Override
    public Collection<ProductRatingSnapshot> fetchAll(
            final Instant rangeStartTime,
            final Instant rangeEndTime) {
        final var allRatings = new ArrayList<ProductRatingSnapshot>();

        int pageNumber = 0;
        boolean hasNextPage = false;

        do {
            final var pageResponse = feignClient.getUpdatedRatingInfos(
                    rangeStartTime,
                    rangeEndTime,
                    pageNumber,
                    PAGE_SIZE);
            final var mappedItems = pageResponse.items().stream()
                    .map(ProductRatingFeignAdapter::toDomainDto)
                    .toList();

            allRatings.addAll(mappedItems);
            hasNextPage = pageResponse.hasNext();
            pageNumber++;
        } while (hasNextPage);

        return List.copyOf(allRatings);
    }

    private static ProductRatingSnapshot toDomainDto(
            final RatingInfoResponse response) {
        return new ProductRatingSnapshot(
                new ProductId(response.productId()),
                new ProductRatingTotal(response.totalPoint()),
                new ProductRatingAmount(response.ratingCount()));
    }
}
