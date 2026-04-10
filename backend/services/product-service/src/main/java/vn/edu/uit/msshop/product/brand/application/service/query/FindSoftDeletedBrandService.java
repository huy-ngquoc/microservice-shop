package vn.edu.uit.msshop.product.brand.application.service.query;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.FindSoftDeletedBrandUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.LoadSoftDeletedBrandPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class FindSoftDeletedBrandService implements FindSoftDeletedBrandUseCase {
    private final LoadSoftDeletedBrandPort loadSoftDeletedPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public BrandView findSoftDeletedById(
            final BrandId id) {
        return this.loadSoftDeletedPort.loadSoftDeletedById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(id));
    }
}
