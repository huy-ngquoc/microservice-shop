package vn.edu.uit.msshop.product.brand.application.service.query.listing;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.query.listing.BrandActiveListingQuery;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.listing.BrandActiveListingUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.brand.query.listing.BrandActiveListingPort;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class BrandActiveListingService
        implements BrandActiveListingUseCase {

    private final BrandActiveListingPort listPort;
    private final BrandViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.BRAND_LIST)
    public PageResponseDto<BrandView> list(
            final BrandActiveListingQuery query) {
        final var page = listPort.list(query.pageRequest());
        return page.map(this.mapper::toView);
    }
}
