package vn.edu.uit.msshop.product.brand.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.existence.BrandActiveExistenceCheckByIdQuery;
import vn.edu.uit.msshop.product.brand.application.port.in.query.existence.BrandActiveExistenceCheckByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.existence.BrandActiveExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class BrandActiveExistenceCheckByIdService
        implements BrandActiveExistenceCheckByIdUseCase {

    private final BrandActiveExistenceCheckByIdPort checkExistsPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean check(
            final BrandActiveExistenceCheckByIdQuery query) {
        final var brandId = new BrandId(query.brandId());
        return this.checkExistsPort.existsById(brandId);
    }
}
