package vn.edu.uit.msshop.product.product.application.service.query.lookup;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.ProductActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductActiveLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class ProductActiveLookupService
        implements ProductActiveLookupByIdUseCase {
    private final ProductActiveLookupByIdPort activeLookupByIdPort;
    private final ProductSoldCountLookupByIdPort soldCountLookupByIdPort;
    private final ProductStockCountLookupByIdPort stockCountLookupByIdPort;
    private final ProductRatingLookupByIdPort ratingLookupByIdPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.PRODUCT,
            key = "#id.value()")
    public ProductView findById(
            final ProductId id) {
        final var product = this.activeLookupByIdPort.loadById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        final var soldCount = this.soldCountLookupByIdPort.loadByIdOrZero(id);
        final var stockCount = this.stockCountLookupByIdPort.loadByIdOrZero(id);
        final var rating = this.ratingLookupByIdPort.loadByIdOrZero(id);

        return this.mapper.toView(product, soldCount, stockCount, rating);
    }
}
