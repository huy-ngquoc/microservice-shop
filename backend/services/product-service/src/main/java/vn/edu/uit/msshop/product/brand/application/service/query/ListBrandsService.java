package vn.edu.uit.msshop.product.brand.application.service.query;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.brand.application.dto.view.BrandView;
import vn.edu.uit.msshop.product.brand.application.mapper.BrandViewMapper;
import vn.edu.uit.msshop.product.brand.application.port.in.query.ListBrandsUseCase;
import vn.edu.uit.msshop.product.brand.application.port.out.persistence.ListBrandsPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class ListBrandsService implements ListBrandsUseCase {
    private final ListBrandsPort listPort;
    private final BrandViewMapper viewMapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.BRAND_LIST)
    public PageResponseDto<BrandView> list(
            PageRequestDto pageRequest) {
        final var page = listPort.list(pageRequest);
        return page.map(this.viewMapper::toView);
    }
}
