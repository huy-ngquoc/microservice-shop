package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper.CategoryPersistenceMapper;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.CheckCategoryExistsPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.CreateCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.DeleteCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.ListCategoriesPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.ListSoftDeletedCategoriesPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.LoadSoftDeletedCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.creation.NewCategory;
import vn.edu.uit.msshop.product.category.domain.model.valueobject.CategoryId;
import vn.edu.uit.msshop.shared.adapter.out.persistence.PageRequests;
import vn.edu.uit.msshop.shared.application.dto.request.PageRequestDto;
import vn.edu.uit.msshop.shared.application.dto.response.PageResponseDto;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter
        implements
        ListCategoriesPort,
        ListSoftDeletedCategoriesPort,
        LoadCategoryPort,
        LoadSoftDeletedCategoryPort,
        CheckCategoryExistsPort,
        CreateCategoryPort,
        UpdateCategoryPort,
        DeleteCategoryPort {
    private final CategoryMongoRepository repository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public PageResponseDto<Category> list(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(
                pageRequest,
                CategoryDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNull(pageable);

        final var categories = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                categories,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public PageResponseDto<Category> listSoftDeleted(
            final PageRequestDto pageRequest) {
        final var pageable = PageRequests.toPageable(
                pageRequest,
                CategoryDocument.Fields.id);
        final var page = this.repository.findAllByDeletionTimeIsNotNull(pageable);

        final var categories = page.getContent().stream()
                .map(this.mapper::toDomain)
                .toList();

        return new PageResponseDto<>(
                categories,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements());
    }

    @Override
    public Optional<Category> loadById(
            final CategoryId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public Optional<Category> loadSoftDeletedById(
            final CategoryId id) {
        final var jpaId = id.value();
        return this.repository.findByIdAndDeletionTimeIsNotNull(jpaId)
                .map(this.mapper::toDomain);
    }

    @Override
    public boolean existsById(
            final CategoryId id) {
        final var jpaId = id.value();
        return this.repository.existsByIdAndDeletionTimeIsNull(jpaId);
    }

    @Override
    public Category create(
            final NewCategory newCategory) {
        final var toSave = this.mapper.toPersistence(newCategory);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public Category update(
            final Category category) {
        final var toSave = this.mapper.toPersistence(category);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }

    @Override
    public void deleteById(
            final CategoryId id) {
        final var jpaId = id.value();
        this.repository.deleteById(jpaId);
    }
}
