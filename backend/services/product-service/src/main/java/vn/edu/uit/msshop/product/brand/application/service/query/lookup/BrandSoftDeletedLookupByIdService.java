package vn.edu.uit.msshop.product.brand.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.lookup.BrandSoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.lookup.BrandSoftDeletedLookupByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandSoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class BrandSoftDeletedLookupByIdService
        implements BrandSoftDeletedLookupByIdUseCase {

    private final BrandSoftDeletedLookupByIdPort loadSoftDeletedPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public BrandView find(
            final BrandSoftDeletedLookupByIdQuery query) {
        final var brandId = new BrandId(query.brandId());
        return this.loadSoftDeletedPort.loadSoftDeletedById(brandId)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }
}
