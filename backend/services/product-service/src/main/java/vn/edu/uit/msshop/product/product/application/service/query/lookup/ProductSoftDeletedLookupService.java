package vn.edu.uit.msshop.product.product.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.view.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.lookup.FindSoftDeletedProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.count.query.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.LoadSoftDeletedProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.rating.query.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class ProductSoftDeletedLookupService
        implements FindSoftDeletedProductUseCase {
    private final LoadSoftDeletedProductPort loadSoftDeletedPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public ProductView findSoftDeletedById(
            final ProductId id) {
        final var product = this.loadSoftDeletedPort.loadSoftDeletedById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(id);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(id);
        final var rating = this.loadRatingPort.loadByIdOrZero(id);

        return this.mapper.toView(product, soldCount, stockCount, rating);
    }

}
