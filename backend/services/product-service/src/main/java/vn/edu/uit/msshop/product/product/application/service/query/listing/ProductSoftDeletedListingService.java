package vn.edu.uit.msshop.product.product.application.service.query.listing;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.listing.ProductSoftDeletedListingQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.listing.ProductSoftDeletedListingUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountBulkLookupByProductIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountBulkLookupByProductIdsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.listing.ProductSoftDeletedListingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingBulkLookupByProductIdsPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
class ProductSoftDeletedListingService
        implements ProductSoftDeletedListingUseCase {

    private final ProductSoftDeletedListingPort softDeletedListingPort;
    private final ProductSoldCountBulkLookupByProductIdsPort soldCountBulkLookupByProductIdsPort;
    private final ProductStockCountBulkLookupByProductIdsPort stockCountBulkLookupByProductIdsPort;
    private final ProductRatingBulkLookupByProductIdsPort ratingBulkLookupByProductIdsPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<ProductView> list(
            final ProductSoftDeletedListingQuery query) {
        final var page = this.softDeletedListingPort.listSoftDeleted(query.pageRequest());

        final var productIdSet = page.items().stream()
                .map(Product::getId)
                .collect(Collectors.toUnmodifiableSet());

        final var soldCountByProductId = this.soldCountBulkLookupByProductIdsPort.loadAllByProductIds(productIdSet);
        final var stockCountByProductId = this.stockCountBulkLookupByProductIdsPort.loadAllByProductIds(productIdSet);
        final var ratingByProductId = this.ratingBulkLookupByProductIdsPort.loadAllByProductIds(productIdSet);

        return page.map(p -> this.toView(
                p,
                soldCountByProductId,
                stockCountByProductId,
                ratingByProductId));
    }

    // TODO: Duplicate with ProductActiveListingService.
    // Move it to somewhere that all can use.
    private ProductView toView(
            Product product,
            Map<ProductId, ProductSoldCount> soldCountByProductId,
            Map<ProductId, ProductStockCount> stockCountByProductId,
            Map<ProductId, ProductRating> ratingByProductId) {
        final var productId = product.getId();

        final var soldCount = soldCountByProductId.getOrDefault(
                productId,
                ProductSoldCount.zero(productId));
        final var stockCount = stockCountByProductId.getOrDefault(productId,
                ProductStockCount.zero(productId));
        final var rating = ratingByProductId.getOrDefault(
                productId,
                ProductRating.zero(productId));

        return this.mapper.toView(
                product,
                soldCount,
                stockCount,
                rating);
    }
}
