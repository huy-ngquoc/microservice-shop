package vn.edu.uit.msshop.product.category.application.service.query.lookup;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategoryActiveLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.lookup.CategoryActiveLookupByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategoryActiveLookupByIdPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
class CategoryActiveLookupByIdService
        implements CategoryActiveLookupByIdUseCase {

    private final CategoryActiveLookupByIdPort loadPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.CATEGORY,
            key = "#id.value()")
    public CategoryView find(
            final CategoryActiveLookupByIdQuery query) {
        final var categoryId = new CategoryId(query.categoryId());
        return this.loadPort.loadActiveById(categoryId)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
