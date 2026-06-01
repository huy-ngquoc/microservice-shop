package vn.edu.uit.msshop.product.category.application.service.query;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.bootstrap.config.cache.CacheNames;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryImageView;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.CheckCategoryExistsUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindCategoryImageUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.FindSoftDeletedCategoryUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.ListCategoriesUseCase;
import vn.edu.uit.msshop.product.category.application.port.in.query.ListSoftDeletedCategoriesUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.CheckCategoryExistsPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.ListCategoriesPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.ListSoftDeletedCategoriesPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Service
@RequiredArgsConstructor
public class CategoryLookupService
        implements
        ListCategoriesUseCase,
        ListSoftDeletedCategoriesUseCase,
        CheckCategoryExistsUseCase,
        FindCategoryUseCase,
        FindCategoryImageUseCase,
        FindSoftDeletedCategoryUseCase {

    private final ListCategoriesPort listPort;
    private final ListSoftDeletedCategoriesPort listSoftDeletedPort;
    private final CheckCategoryExistsPort checkExistsPort;
    private final LoadCategoryPort loadPort;
    private final LoadSoftDeletedCategoryPort loadSoftDeletedPort;

    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.CATEGORY_LIST)
    public PageResponseDto<CategoryView> list(
            final PageRequestDto pageRequest) {
        final var page = this.listPort.list(pageRequest);
        return page.map(this.mapper::toView);
    }

    @Override
    @Transactional(
            readOnly = true)
    public PageResponseDto<CategoryView> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var page = this.listSoftDeletedPort.listSoftDeleted(pageRequest);
        return page.map(this.mapper::toView);
    }

    @Override
    @Transactional(
            readOnly = true)
    public boolean existsById(
            final CategoryId id) {
        return this.checkExistsPort.existsById(id);
    }

    @Override
    @Transactional(
            readOnly = true)
    @Cacheable(
            cacheNames = CacheNames.CATEGORY,
            key = "#id.value()")
    public CategoryView findById(
            final CategoryId id) {
        return this.loadPort.loadById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    @Transactional(
            readOnly = true)
    public CategoryImageView findImageById(
            final CategoryId id) {
        return this.loadPort.loadById(id).map(this.mapper::toImageView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    @Transactional(
            readOnly = true)
    public CategoryView findSoftDeletedById(
            final CategoryId id) {
        return this.loadSoftDeletedPort.loadSoftDeletedById(id)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }
}
