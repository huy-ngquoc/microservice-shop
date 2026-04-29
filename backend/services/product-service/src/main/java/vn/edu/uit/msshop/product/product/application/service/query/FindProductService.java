package vn.edu.uit.msshop.product.product.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.ProductView;
import vn.edu.uit.msshop.product.product.application.exception.ProductNotFoundException;
import vn.edu.uit.msshop.product.product.application.mapper.ProductViewMapper;
import vn.edu.uit.msshop.product.product.application.port.in.query.FindProductUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductRatingPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductSoldCountPort;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.LoadProductStockCountPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductId;

@Service
@RequiredArgsConstructor
public class FindProductService implements FindProductUseCase {
    private final LoadProductPort loadPort;
    private final LoadProductSoldCountPort loadSoldCountPort;
    private final LoadProductStockCountPort loadStockCountPort;
    private final LoadProductRatingPort loadRatingPort;
    private final ProductViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public ProductView findById(
            final ProductId id) {
        final var product = this.loadPort.loadById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        final var soldCount = this.loadSoldCountPort.loadByIdOrZero(id);
        final var stockCount = this.loadStockCountPort.loadByIdOrZero(id);
        final var rating = this.loadRatingPort.loadByIdOrZero(id);

        return this.mapper.toView(
                product,
                soldCount,
                stockCount,
                rating);
    }
}
