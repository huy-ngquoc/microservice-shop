package vn.edu.uit.msshop.product.product.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.lookup.ProductSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.ProductSoftDeletedLookupByIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductSoldCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.ProductStockCountLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.lookup.ProductSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.ProductRatingLookupByProductIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
class ProductSoftDeletedLookupService
        implements ProductSoftDeletedLookupByIdUseCase {
    private final ProductSoftDeletedLookupByIdPort softDeletedLookupByIdPort;
    private final ProductSoldCountLookupByProductIdPort soldCountLookupByProductIdPort;
    private final ProductStockCountLookupByProductIdPort stockCountLookupByProductIdPort;
    private final ProductRatingLookupByProductIdPort ratingLookupByProductIdPort;

    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public ProductView find(
            final ProductSoftDeletedLookupByIdQuery query) {
        final var productId = new ProductId(query.productId());
        final var product = this.softDeletedLookupByIdPort.loadSoftDeletedById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        final var soldCount = this.soldCountLookupByProductIdPort.loadByProductIdOrZero(productId);
        final var stockCount = this.stockCountLookupByProductIdPort.loadByProductIdOrZero(productId);
        final var rating = this.ratingLookupByProductIdPort.loadByProductIdOrZero(productId);

        return this.mapper.toView(
                product,
                soldCount,
                stockCount,
                rating);
    }

}
