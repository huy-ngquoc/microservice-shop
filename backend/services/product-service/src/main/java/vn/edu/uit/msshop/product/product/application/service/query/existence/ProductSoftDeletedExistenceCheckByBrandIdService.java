package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.dto.query.existence.ProductSoftDeletedExistenceCheckByBrandIdQuery;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductSoftDeletedExistenceCheckByBrandIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductSoftDeletedExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Service
@RequiredArgsConstructor
class ProductSoftDeletedExistenceCheckByBrandIdService
        implements ProductSoftDeletedExistenceCheckByBrandIdUseCase {

    private final ProductSoftDeletedExistenceCheckByBrandIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean exists(
            final ProductSoftDeletedExistenceCheckByBrandIdQuery query) {
        final var brandId = new ProductBrandId(query.brandId());
        return this.existenceCheckPort.existsSoftDeletedByBrandId(brandId);
    }
}
