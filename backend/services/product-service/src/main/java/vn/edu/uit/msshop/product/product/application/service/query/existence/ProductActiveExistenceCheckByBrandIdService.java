package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductActiveExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductActiveExistenceCheckByBrandIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Service
@RequiredArgsConstructor
public class ProductActiveExistenceCheckByBrandIdService
        implements ProductActiveExistenceCheckByBrandIdUseCase {

    private final ProductExistenceCheckByBrandIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean exists(
            final ProductActiveExistenceCheckByBrandIdQuery query) {
        final var brandId = new ProductBrandId(query.brandId());
        return this.existenceCheckPort.existsByBrandId(brandId);
    }
}
