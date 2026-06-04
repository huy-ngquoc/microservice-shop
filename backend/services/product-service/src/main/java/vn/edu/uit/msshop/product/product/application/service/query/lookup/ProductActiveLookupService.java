package vn.edu.uit.msshop.product.product.application.service.query.lookup;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class ProductActiveLookupService
        implements FindProductUseCase {
    private final LoadProductPort loadPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.PRODUCT,
            key = "#id.value()")
    public ProductView findById(
            final ProductId id) {
        final var product = this.loadPort.loadById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(id);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(id);
        final var rating = this.loadRatingPort.loadByIdOrZero(id);

        return this.mapper.toView(product, soldCount, stockCount, rating);
    }
}
