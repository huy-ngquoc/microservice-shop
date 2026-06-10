package vn.edu.uit.msshop.product.brand.application.service.query.lookup;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.query.lookup.BrandActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.exception.BrandNotFoundException;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.lookup.BrandActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.lookup.BrandActiveLookupByIdPort;
import vn.edu.uit.msshop.product.brand.domain.model.valueobject.BrandId;

@Service
@RequiredArgsConstructor
class BrandActiveLookupByIdService
        implements BrandActiveLookupByIdUseCase {

    private final BrandActiveLookupByIdPort loadPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.BRAND,
            key = "#query.brandId()")
    public BrandView find(
            final BrandActiveLookupByIdQuery query) {
        final var brandId = new BrandId(query.brandId());
        return this.loadPort.loadById(brandId)
                .map(this.mapper::toView)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }
}
