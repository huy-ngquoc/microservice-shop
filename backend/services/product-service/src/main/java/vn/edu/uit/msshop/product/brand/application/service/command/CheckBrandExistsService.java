package vn.edu.uit.msshop.product.brand.application.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.port.in.query.CheckBrandExistsUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.CheckBrandExistsPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class CheckBrandExistsService implements CheckBrandExistsUseCase {
    private final CheckBrandExistsPort checkExistsPort;

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final BrandId id) {
        return this.checkExistsPort.existsById(id);
    }
}
