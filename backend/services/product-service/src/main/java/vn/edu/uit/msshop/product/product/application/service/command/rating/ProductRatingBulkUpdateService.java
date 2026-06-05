package vn.edu.uit.msshop.product.product.application.service.command.rating;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.SetAllProductRatingsCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingBulkUpdatePort;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.sync.ProductRatingSnapshot;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRatingBulkUpdateService
        implements
        ProductRatingBulkUpdateUseCase {
    private final ProductRatingBulkUpdatePort ratingBulkUpdatePort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            allEntries = true,
                            condition = "!#command.ratings().isEmpty()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true,
                            condition = "!#command.ratings().isEmpty()"),
            })
    public void execute(
            final SetAllProductRatingsCommand command) {
        final var ratings = command.ratings();
        if (ratings.isEmpty()) {
            return;
        }

        final var next = ratings.stream()
                .map(ProductRatingBulkUpdateService::toNext)
                .toList();
        this.ratingBulkUpdatePort.updateAll(next);
    }

    private static ProductRating toNext(
            final ProductRatingSnapshot snapshot) {
        return new ProductRating(
                snapshot.productId(),
                snapshot.total(),
                snapshot.amount());
    }
}
