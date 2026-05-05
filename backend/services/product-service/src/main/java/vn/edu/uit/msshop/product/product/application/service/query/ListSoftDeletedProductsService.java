package vn.edu.uit.msshop.product.product.application.service.query;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.ListSoftDeletedProductsUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.ListSoftDeletedProductsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductRatingsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductSoldCountsPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadAllProductStockCountsPort;
import vn.edu.uit.msshop.product.product.domain.model.Product;
import vn.edu.uit.msshop.product.product.domain.model.ProductRating;
import vn.edu.uit.msshop.product.product.domain.model.ProductSoldCount;
import vn.edu.uit.msshop.product.product.domain.model.ProductStockCount;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListSoftDeletedProductsService
        implements ListSoftDeletedProductsUseCase {
    private static final Collector<ProductId, ?, Set<ProductId>> SET_COLLECTOR = Collectors.toSet();

    private final ListSoftDeletedProductsPort listSoftDeletedPort;
    private final LoadAllProductSoldCountsPort loadAllSoldCountsPort;
    private final LoadAllProductStockCountsPort loadAllStockCountsPort;
    private final LoadAllProductRatingsPort loadAllRatingsPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<ProductView> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var page = this.listSoftDeletedPort.listSoftDeleted(pageRequest);

        final var ids = page.items().stream()
                .map(Product::getId)
                .collect(SET_COLLECTOR);

        final var soldCountById = this.loadAllSoldCountsPort.loadAllByIds(ids);
        final var stockCountById = this.loadAllStockCountsPort.loadAllByIds(ids);
        final var ratingById = this.loadAllRatingsPort.loadAllByIds(ids);

        return page.map(p -> this.toView(
                p,
                soldCountById,
                stockCountById,
                ratingById));
    }

    // TODO: Duplicate with ListProductsService.
    // Move it to somewhere that all can use.
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
