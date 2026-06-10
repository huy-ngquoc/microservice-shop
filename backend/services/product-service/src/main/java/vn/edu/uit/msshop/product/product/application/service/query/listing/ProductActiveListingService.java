package vn.edu.uit.msshop.product.product.application.service.query.listing;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductActiveListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.listing.ProductActiveListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductActiveListingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingBulkLookupByIdsPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ProductActiveListingService
        implements ProductActiveListingUseCase {
    private static final Collector<
            ProductId,
            ?,
            Set<ProductId>> SET_COLLECTOR = Collectors.toSet();

    private final ProductActiveListingPort activeListingPort;
    private final ProductSoldCountBulkLookupByIdsPort soldCountBulkLookupByIdsPort;
    private final ProductStockCountBulkLookupByIdsPort stockCountBulkLookupByIdsPort;
    private final ProductRatingBulkLookupByIdsPort ratingBulkLookupByIdsPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.PRODUCT_LIST)
    public PageResponseDto<ProductView> list(
            final ProductActiveListingQuery query) {
        final var page = this.activeListingPort.list(query.pageRequest());

        final var ids = page.items().stream()
                .map(Product::getId)
                .collect(SET_COLLECTOR);

        final var soldCountById = this.soldCountBulkLookupByIdsPort.loadAllByIds(ids);
        final var stockCountById = this.stockCountBulkLookupByIdsPort.loadAllByIds(ids);
        final var ratingById = this.ratingBulkLookupByIdsPort.loadAllByIds(ids);

        return page.map(p -> this.toView(
                p,
                soldCountById,
                stockCountById,
                ratingById));
    }

    private ProductView toView(
            Product product,
            Map<ProductId, ProductSoldCount> soldCountById,
            Map<ProductId, ProductStockCount> stockCountById,
            Map<ProductId, ProductRating> ratingById) {
        final var productId = product.getId();

        final var soldCount = soldCountById.getOrDefault(
                productId,
                ProductSoldCount.zero(productId));
        final var stockCount = stockCountById.getOrDefault(
                productId,
                ProductStockCount.zero(productId));
        final var rating = ratingById.getOrDefault(
                productId,
                ProductRating.zero(productId));

        return this.mapper.toView(
                product,
                soldCount,
                stockCount,
                rating);
    }
}
