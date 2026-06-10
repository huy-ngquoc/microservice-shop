package vn.edu.uit.msshop.product.product.application.service.command.rating;

import java.util.ArrayList;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.command.rating.ProductRatingBulkUpdateCommand;
import vn.edu.uit.msshop.product.product.application.port.in.command.rating.ProductRatingBulkUpdateUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.command.ProductRatingBulkUpdatePort;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingAmount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductRatingTotal;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductRatingBulkUpdateService
        implements ProductRatingBulkUpdateUseCase {
    private final ProductRatingBulkUpdatePort ratingBulkUpdatePort;

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT,
                            allEntries = true,
                            condition = "!#cmd.ratingList().isEmpty()"),
                    @CacheEvict(
                            cacheNames = CacheNames.PRODUCT_LIST,
                            allEntries = true,
                            condition = "!#cmd.ratingList().isEmpty()"),
            })
    public void execute(
            final ProductRatingBulkUpdateCommand cmd) {
        final var ratingList = cmd.ratingList();
        if (ratingList.isEmpty()) {
            return;
        }

        final var productRatingList = new ArrayList<ProductRating>(ratingList.size());
        for (final var ratingData : ratingList) {
            final var productRating = new ProductRating(
                    new ProductId(ratingData.productId()),
                    new ProductRatingTotal(ratingData.total()),
                    new ProductRatingAmount(ratingData.amount()));
            productRatingList.add(productRating);
        }

        this.ratingBulkUpdatePort.updateAll(productRatingList);
    }
}
