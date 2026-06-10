package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryCreationPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryDeletionByIdPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.command.CategoryUpdatePort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.existence.CategoryActiveExistenceCheckByIdPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.listing.CategoryActiveListingPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.listing.CategorySoftDeletedListingPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategoryActiveLookupByIdPort;
import vn.edu.uit.msshop.product.category.application.port.out.persistence.category.query.lookup.CategorySoftDeletedLookupByIdPort;
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
        CategoryActiveListingPort,
        CategorySoftDeletedListingPort,
        CategoryActiveLookupByIdPort,
        CategorySoftDeletedLookupByIdPort,
        CategoryActiveExistenceCheckByIdPort,
        CategoryCreationPort,
        CategoryUpdatePort,
        CategoryDeletionByIdPort {
    private final CategoryMongoRepository repository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public PageResponseDto<Category> listActive(
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
    public Optional<Category> loadActiveById(
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
    public boolean existsActiveById(
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
