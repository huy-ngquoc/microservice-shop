package vn.edu.uit.msshop.product.category.application.service.query.listing;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.query.listing.CategoryActiveListingQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.listing.CategoryActiveListingUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.listing.CategoryActiveListingPort;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class CategoryActiveListingService
        implements CategoryActiveListingUseCase {

    private final CategoryActiveListingPort listPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.CATEGORY_LIST)
    public PageResponseDto<CategoryView> list(
            final CategoryActiveListingQuery query) {
        final var page = this.listPort.listActive(query.pageRequest());
        return page.map(this.mapper::toView);
    }
}
