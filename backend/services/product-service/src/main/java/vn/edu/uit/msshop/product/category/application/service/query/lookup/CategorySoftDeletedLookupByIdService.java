package vn.edu.uit.msshop.product.category.application.service.query.lookup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.dto.query.lookup.CategorySoftDeletedLookupByIdQuery;
import vn.edu.uit.msshop.product.category.application.dto.view.CategoryView;
import vn.edu.uit.msshop.product.category.application.exception.CategoryNotFoundException;
import vn.edu.uit.msshop.product.category.application.mapper.CategoryViewMapper;
import vn.edu.uit.msshop.product.category.application.port.in.query.lookup.CategorySoftDeletedLookupByIdUseCase;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategorySoftDeletedLookupByIdPort;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;

@Service
@RequiredArgsConstructor
public class CategorySoftDeletedLookupByIdService
        implements CategorySoftDeletedLookupByIdUseCase {

    private final CategorySoftDeletedLookupByIdPort loadSoftDeletedPort;
    private final CategoryViewMapper mapper;

    @Override
    @Transactional(
            readOnly = true)
    public CategoryView find(
            final CategorySoftDeletedLookupByIdQuery query) {
        final var categoryId = new CategoryId(query.categoryId());
        return this.loadSoftDeletedPort.loadSoftDeletedById(categoryId)
                .map(this.mapper::toView)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
