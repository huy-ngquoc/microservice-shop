package vn.edu.uit.msshop.product.product.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.product.application.port.in.query.existence.ProductSoftDeletedExistenceCheckByBrandIdUseCase;
import vn.edu.uit.msshop.product.product.application.port.out.persistence.product.query.existence.ProductSoftDeletedExistenceCheckByBrandIdPort;
import vn.edu.uit.msshop.product.product.domain.model.valueobject.ProductBrandId;

@Service
@RequiredArgsConstructor
public class ProductSoftDeletedExistenceCheckByBrandIdService
        implements ProductSoftDeletedExistenceCheckByBrandIdUseCase {

    private final ProductSoftDeletedExistenceCheckByBrandIdPort existenceCheckPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsByBrandId(
            final ProductBrandId brandId) {
        return this.existenceCheckPort.existsSoftDeletedByBrandId(brandId);
    }
}
