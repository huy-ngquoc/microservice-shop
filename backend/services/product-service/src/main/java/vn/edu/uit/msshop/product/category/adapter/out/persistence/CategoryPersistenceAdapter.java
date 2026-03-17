package vn.edu.uit.msshop.product.category.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.category.adapter.out.persistence.mapper.CategoryPersistenceMapper;
import vn.edu.uit.msshop.product.category.application.port.out.CreateCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.category.application.port.out.UpdateCategoryPort;
import vn.edu.uit.msshop.product.category.domain.model.Category;
import vn.edu.uit.msshop.product.category.domain.model.CategoryId;
import vn.edu.uit.msshop.product.category.domain.model.NewCategory;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter
        implements LoadCategoryPort, CreateCategoryPort, UpdateCategoryPort {
    private final CategoryMongoRepository repository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public Optional<Category> loadById(
            final CategoryId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
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
}
