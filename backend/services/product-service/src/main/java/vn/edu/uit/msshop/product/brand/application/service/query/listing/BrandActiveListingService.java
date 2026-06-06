package vn.edu.uit.msshop.product.brand.application.service.query.listing;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.BrandLookupUseCases;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListBrandsPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class BrandActiveListingService
        implements BrandLookupUseCases.ListActive {

    private final ListBrandsPort listPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.BRAND_LIST)
    public PageResponseDto<BrandView> listActive(
            final PageRequestDto pageRequest) {
        final var page = listPort.list(pageRequest);
        return page.map(this.mapper::toView);
    }
}
