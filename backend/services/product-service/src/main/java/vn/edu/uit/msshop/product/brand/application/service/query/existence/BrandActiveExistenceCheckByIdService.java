package vn.edu.uit.msshop.product.brand.application.service.query.existence;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CheckBrandExistsPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class BrandActiveExistenceCheckByIdService
        implements BrandLookupUseCases.CheckExistsById {

    private final CheckBrandExistsPort checkExistsPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final BrandId id) {
        return this.checkExistsPort.existsById(id);
    }
}
