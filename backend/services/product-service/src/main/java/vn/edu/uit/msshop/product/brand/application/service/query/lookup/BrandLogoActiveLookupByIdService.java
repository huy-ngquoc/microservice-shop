package vn.edu.uit.msshop.product.brand.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.brand.application.dto.query.lookup.BrandLogoActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandLogoView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.lookup.BrandLogoActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandActiveLookupByIdPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
public class BrandLogoActiveLookupByIdService
        implements BrandLogoActiveLookupByIdUseCase {

    private final BrandActiveLookupByIdPort loadPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public BrandLogoView find(
            final BrandLogoActiveLookupByIdQuery query) {
        final var brandId = new BrandId(query.brandId());
        return this.loadPort.loadById(brandId)
                .map(this.mapper::toLogoView)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }
}
