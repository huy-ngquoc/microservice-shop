package vn.edu.uit.msshop.product.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.product.adapter.out.persistence.mapper.CategoryEntityMapper;
import vn.edu.uit.msshop.product.application.port.out.LoadCategoryPort;
import vn.edu.uit.msshop.product.application.port.out.SaveCategoryPort;
import vn.edu.uit.msshop.product.domain.model.category.Category;
import vn.edu.uit.msshop.product.domain.model.category.valueobject.CategoryId;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter
        implements LoadCategoryPort, SaveCategoryPort {
    private final SpringDataCategoryJpaRepository repository;
    private final CategoryEntityMapper mapper;

    @Override
    public Optional<Category> loadById(
            final CategoryId id) {
        final var jpaId = id.value();
        return this.repository.findById(jpaId).map(this.mapper::toDomain);
    }

    @Override
    public Category save(
            final Category category) {
        final var toSave = this.mapper.toEntity(category);
        final var saved = this.repository.save(toSave);
        return this.mapper.toDomain(saved);
    }
}
